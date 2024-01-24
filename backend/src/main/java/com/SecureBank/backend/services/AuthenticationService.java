package com.SecureBank.backend.services;

import com.SecureBank.backend.algorithms.EntropyCalculator;
import com.SecureBank.backend.controllers.AuthenticationController;
import com.SecureBank.backend.entities.Account;
import com.SecureBank.backend.entities.ActiveSession;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.entities.BankUserCredentials;
import com.SecureBank.backend.entities.UserPassCharCombination;
import com.SecureBank.backend.repositiories.ActiveSessionRepository;
import com.SecureBank.backend.repositiories.BankUserRepository;
import com.SecureBank.backend.repositiories.UserPassCharCombinationsRepository;
import com.sun.jdi.InternalException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final ActiveSessionRepository activeSessionRepository;
  private final BankUserRepository bankUserRepository;
  private final UserPassCharCombinationsRepository userPassCharCombinationsRepository;
  private final PatternLoginService patternLoginService;
  private final EntropyCalculator entropyCalculator;
  private final SameUserLoginService sameUserLoginService;
  private final CredentialsCipher credentialsCipher;
  private final AccountCreator accountCreator;
  public static final String SESSION_COOKIE_NAME = "sessionId";
  private static final int SESSION_MAX_LIFE_TIME = 120;   //time in seconds basic - 1200 (20 min)

  private static final int SESSION_ID_LENGTH_IN_BYTES = 128;

  private static final int SALT_LENGTH_IN_BYTES = 16;
  private static final double MINIMAL_PASSWORD_ENTROPY = 80;
  private static final int MINIMAL_PASSWORD_LENGTH = 16;

  public String registerUser(String username, String password, String name, String surname, String identificationNumber){
    byte [] passwordByteFormat= password.getBytes(StandardCharsets.UTF_8);
    byte [] passwordSalt = generateSecureRandomBytes(SALT_LENGTH_IN_BYTES);
    byte [] passwordHashed = hashData(passwordByteFormat, passwordSalt);

    if (bankUserRepository.existsByUsername(username)){
      throw new RuntimeException("Provided username is already taken");
    }

    double passwordEntropy = entropyCalculator.calculateEntropy(password);
    String entropyCategory = entropyCalculator.checkEntropyCategory(passwordEntropy);
    String infoMessage = "Provided password strength: " + entropyCategory;
    if(password.length() < MINIMAL_PASSWORD_LENGTH){
      infoMessage+="Provided password is too short!";
    }
    else if(passwordEntropy < MINIMAL_PASSWORD_ENTROPY ){
        infoMessage += " Provide stronger password to register! You can use uppercase and lowercase letters, special characters and digits!";
    } else {
      infoMessage+="\n Successfully registered, you can log in now!";

      BankUserCredentials bankUserCredentials = credentialsCipher.encryptCredentials(name, surname, identificationNumber);

      BankUser newBankUser = new BankUser(username, passwordHashed, passwordSalt, bankUserCredentials);
      bankUserRepository.save(newBankUser);
      accountCreator.createNewAccount(newBankUser);
      patternLoginService.generatePassCharCombinations(username, password, passwordSalt);
    }

    return infoMessage;
  }

  @Transactional
  public String login(String username, String password, boolean selectedPartialPassLogin){
    BankUser bankUser = bankUserRepository.findByUsername(username).orElseThrow( () -> new NoSuchElementException("User with this username does not exist"));

    sameUserLoginService.saveLoginAttempt(username);
    if(!sameUserLoginService.isLoginWithThisUsernameAllowed(username)){
      throw new RuntimeException("Exceeded max login attempts (" + sameUserLoginService.MAX_LOGIN_ATTEMPTS_IN_TIME_PERIOD
          + "), wait " + sameUserLoginService.LOGIN_TIME_PERIOD + " seconds before next attempt");
    }

    byte[] passwordHashInDb = bankUser.getPassword();
    String sessionId = null;

    byte [] providedPasswordByteFormat = password.getBytes();
    byte [] providedPasswordHash = hashData(providedPasswordByteFormat, bankUser.getPasswordSalt());

    System.out.println("Pass in db: " + new String(passwordHashInDb));
    System.out.println("Pass provided by user: " + new String(providedPasswordHash));

    if(!selectedPartialPassLogin) {
      if (!Arrays.equals(providedPasswordHash, passwordHashInDb)) {
        throw new RuntimeException("Standard login failed - wrong password!");
      }
    } else {
      UserPassCharCombination userPassCharCombination = userPassCharCombinationsRepository.findBySelected(true);
          if(userPassCharCombination == null){
            throw new RuntimeException("User didnt ask for character numbers");
          }
      byte [] combinationHash = userPassCharCombination.getCombinationHash();
      userPassCharCombination.setSelected(false);
      userPassCharCombinationsRepository.save(userPassCharCombination);
      userPassCharCombinationsRepository.flush();

      if(!Arrays.equals(combinationHash, providedPasswordHash)){
        throw new RuntimeException("Partial login failed - wrong characters were provided");
        }

      }

     if (activeSessionRepository.existsByBankUser(bankUser)) {
        ActiveSession activeSession = activeSessionRepository.findByBankUser(bankUser);
//        System.out.println(activeSession.getId());
//        System.out.println(activeSession.getExpirationDate().isBefore(LocalDateTime.now()));
//        System.out.println(Duration.between(activeSession.getCreatedAt(), LocalDateTime.now()).toSeconds() > SESSION_MAX_LIFE_TIME);
        if(activeSession.getExpirationDate().isBefore(LocalDateTime.now()) ||
            Duration.between(activeSession.getCreatedAt(), LocalDateTime.now()).toSeconds() > SESSION_MAX_LIFE_TIME){  //check if active session is expired and delete it (it lasted from situation
          //System.out.println("DELETE session");
          //activeSessionRepository.deleteById(activeSession.getId());                 // when user lost his cookie and went to login (not to endpoint with authentication before) so session was not dumped
          activeSessionRepository.deleteByBankUser(bankUser);
          //System.out.println("After DELETE session");
          activeSessionRepository.flush();  //makes delete affect repo before transaction ends to enable saving new sessionId
          //System.out.println(activeSessionRepository.existsByBankUser(bankUser));     //without this line code doesnt work
        } else {                                                                // if active session not expired user is already ogged
          throw new RuntimeException("Already logged-in");
        }
    }
    String sessionIdBase64Format = generateAndSaveSession(bankUser);

    return sessionIdBase64Format;
  }

  //@Transactional
  public void logoutUser(HttpServletRequest request){
    String [] dataFromRequest = extractSessionIdAndUsernameFromRequest(request);
    String username = dataFromRequest[0];
    String sessionId = dataFromRequest[1];
    if(username == null || sessionId == null){
      throw new RuntimeException("U are not logged in");
    } else {
      activeSessionRepository.deleteByBankUserUsername(username);                 // check if it works
    }

  }

  //TODO: change method name to checkIfUserHasActiveSession
 // @Transactional
  public boolean checkIfUserAuthenticated(HttpServletRequest request){
    boolean isSessionActive = false;

    String [] dataFromCookies = extractSessionIdAndUsernameFromRequest(request);
    String sessionIdBase64Format = dataFromCookies[0];
    String username = dataFromCookies[1];
    //System.out.println(sessionId);

    if(sessionIdBase64Format == null){                                                              //if user doesnt have active session, access is denied and he must login
      throw new RuntimeException("User is not logged in or his sessionId expired");
    } else {
      isSessionActive = isUserSessionActive(sessionIdBase64Format, username);

      if (isSessionActive) {                                                                       // basic session lifetime is 5 min (300 s) from last user action on the webpage

        ActiveSession activeSession = activeSessionRepository.findByBankUserUsername(username)
            .orElseThrow(() -> new InternalException(
                "ErrorInternal: Session is active but cant get it from database "));

        if (isSessionMaxLifeTimeExceeded(activeSession) || isSessionExpired(
            activeSession)) {                                                                   // if session createdAt is more than 20 minutes before ||
          activeSessionRepository.deleteByBankUserUsername(username);                                 // if session is expired (time now > expiration date), user didnt make any action in 5 minutes on authorized endpoints
          isSessionActive = false;

        } else {                                                                                //if user has active session and made action in webpage extend his session lifetime
          extendSession(activeSession);
        }
      }
    }


    return isSessionActive;
  }

  public static String [] extractSessionIdAndUsernameFromRequest(HttpServletRequest request){
    Cookie[] cookies = request.getCookies();
    String [] dataFromCookies = new String[2];
    //System.out.println(cookies[0]);
    //System.out.println(request.getPathInfo());
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
         dataFromCookies[0] = cookie.getValue();
        }
        if (AuthenticationController.USERNAME_COOKIE_NAME.equals(cookie.getName())) {
          dataFromCookies[1] = cookie.getValue();
        }
      }
    }


    return dataFromCookies;
  }

  private boolean isUserSessionActive(String sessionIdBase64Format, String username){
    byte [] sessionIdBytesFormat = convertSessionIdToBytes(sessionIdBase64Format);
    System.out.println("USERNAME: " + username);
    byte [] sessionIdSalt = bankUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User with username provided in cookie does not exist"))
        .getSessionSalt();

    byte [] sessionIdHashed = hashData(sessionIdBytesFormat, sessionIdSalt);

    return activeSessionRepository.existsBySessionIdHashedAndBankUserUsername(sessionIdHashed, username);            //return activeSessionRepository.existsBySessionId(sessionId);
  }

  private void extendSession(ActiveSession activeSession){
    activeSession.setExpirationDate(LocalDateTime.now().plusSeconds(AuthenticationController.SESSION_EXPIRATION_TIME));
    activeSessionRepository.save(activeSession);
  }

  private boolean isSessionMaxLifeTimeExceeded(ActiveSession activeSession){
    Duration duration = Duration.between(activeSession.getCreatedAt(), LocalDateTime.now());
    return duration.toSeconds() > SESSION_MAX_LIFE_TIME;
  }

  private boolean isSessionExpired(ActiveSession activeSession){
    //Duration duration = Duration.between(activeSession.getExpirationDate(), LocalDateTime.now());
    return LocalDateTime.now().isAfter(activeSession.getExpirationDate());
  }

  //temporary implementation for tests
  private String generateAndSaveSession(BankUser bankUser){

    byte[] sessionIdBytesFormat = generateSessionIdAsBytes();
    String sessionIdBase64Format = convertSessionIdToBase64(sessionIdBytesFormat);

    byte[] sessionSalt = generateSecureRandomBytes(SALT_LENGTH_IN_BYTES);
    byte[] sessionIdBytesFormatHash = hashData(sessionIdBytesFormat, sessionSalt);

    activeSessionRepository.save(
        new ActiveSession(0, sessionIdBytesFormatHash, bankUser, LocalDateTime.now().plusSeconds(
            AuthenticationController.SESSION_EXPIRATION_TIME),
            LocalDateTime.now()));  //get sessionId expiration time fro  authenticationControllerClass
    bankUser.setSessionSalt(sessionSalt);
    bankUserRepository.save(bankUser);


    return sessionIdBase64Format;

  }

  private byte[] generateSessionIdAsBytes(){
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[SESSION_ID_LENGTH_IN_BYTES];
    random.nextBytes(bytes);
    return bytes;
  }

  private String convertSessionIdToBase64(byte[] sessionIdBytesFormat){
    return Base64.getEncoder().encodeToString(sessionIdBytesFormat);
  }

  private byte [] convertSessionIdToBytes(String sessionIdBase64Format){
    return Base64.getDecoder().decode(sessionIdBase64Format);
  }

  public static byte[] hashData(byte[] dataByteFormat, byte [] salt){
    byte[] dataByteFormatHashed;
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(salt);
      dataByteFormatHashed = messageDigest.digest(dataByteFormat);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error in hashing password!");
    }
    return dataByteFormatHashed;
  }

  private byte[] generateSecureRandomBytes(int bytesNumber){
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[bytesNumber];
    random.nextBytes(bytes);
    return bytes;
  }
}
