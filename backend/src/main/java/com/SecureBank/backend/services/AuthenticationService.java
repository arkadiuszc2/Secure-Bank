package com.SecureBank.backend.services;

import com.SecureBank.backend.controllers.AuthenticationController;
import com.SecureBank.backend.entities.ActiveSession;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.repositiories.ActiveSessionRepository;
import com.SecureBank.backend.repositiories.BankUserRepository;
import com.sun.jdi.InternalException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final ActiveSessionRepository activeSessionRepository;
  private final BankUserRepository bankUserRepository;
  public static final String SESSION_COOKIE_NAME = "sessionId";
  private static final int SESSION_MAX_LIFE_TIME = 120;   //time in seconds basic - 1200 (20 min)

  public void registerUser(String username, String password){
    //DONE: check if user with this username already exists
    BankUser newBankUser = new BankUser(0, username, password);
    if (bankUserRepository.existsByUsername(username)){
      throw new RuntimeException("Provided username is already taken");
    }
    bankUserRepository.save(newBankUser);
  }

  @Transactional
  public String loginUser(String username, String password, HttpServletRequest request){
    BankUser bankUser = bankUserRepository.findByUsername(username).orElseThrow( () -> new NoSuchElementException("User with this username does not exist"));
    String passwordIndDB = bankUser.getPassword();
    String sessionId = null;

    if(!password.equals(passwordIndDB)){
      throw new RuntimeException("Wrong password");
    }

     if (activeSessionRepository.existsByBankUser(bankUser)) {
        ActiveSession activeSession = activeSessionRepository.findByBankUser(bankUser);
        System.out.println(activeSession.getId());

        System.out.println(activeSession.getExpirationDate().isBefore(LocalDateTime.now()));
        System.out.println(Duration.between(activeSession.getCreatedAt(), LocalDateTime.now()).toSeconds() > SESSION_MAX_LIFE_TIME);
        if(activeSession.getExpirationDate().isBefore(LocalDateTime.now()) ||
            Duration.between(activeSession.getCreatedAt(), LocalDateTime.now()).toSeconds() > SESSION_MAX_LIFE_TIME){  //check if active session is expired and delete it (it lasted from situation
          System.out.println("DELETE session");
          //activeSessionRepository.deleteById(activeSession.getId());                 // when user lost his cookie and went to login (not to endpoint with authentication before) so session was not dumped
          activeSessionRepository.deleteByBankUser(bankUser);
          System.out.println("After DELETE session");
          activeSessionRepository.flush();  //makes delete affect repo before transaction ends to enable saving new sessionId
          //System.out.println(activeSessionRepository.existsByBankUser(bankUser));     //without this line code doesnt work
        } else {                                                                // if active session not expired user is already ogged
          throw new RuntimeException("Already logged-in");
        }
    }

    sessionId = generateSessionId();
    activeSessionRepository.save(
        new ActiveSession(0, sessionId, bankUser, LocalDateTime.now().plusSeconds(
            AuthenticationController.SESSION_EXPIRATION_TIME),
            LocalDateTime.now()));  //get sessionId expiration time fro  authenticationControllerClass


    return sessionId;
  }

  @Transactional
  public void logoutUser(HttpServletRequest request){
    String sessionId = extractSessionIdFromRequest(request);

    if(sessionId == null){
      throw new RuntimeException("U are not logged in");
    } else {
      activeSessionRepository.deleteBySessionId(sessionId);
    }

  }

  //TODO: change method name to checkIfUserHasActiveSession
  @Transactional
  public boolean checkIfUserAuthenticated(HttpServletRequest request){
    boolean isSessionActive = false;

    String sessionId = extractSessionIdFromRequest(request);
    System.out.println(sessionId);

    if(sessionId == null){                                                              //if user doesnt have active session, access is denied and he must login
      throw new RuntimeException("User is not logged in or his sessionId expired");
    } else {
      isSessionActive = isUserSessionActive(sessionId);

      if (isSessionActive) {                                                                       // basic session lifetime is 5 min (300 s) from last user action on the webpage

        ActiveSession activeSession = activeSessionRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new InternalException(
                "ErrorInternal: Session is active but cant get it from database "));

        if (isSessionMaxLifeTimeExceeded(activeSession) || isSessionExpired(
            activeSession)) {                                                                   // if session createdAt is more than 20 minutes before ||
          activeSessionRepository.deleteBySessionId(
              sessionId);                                 // if session is expired (time now > expiration date), user didnt make any action in 5 minutes on authorized endpoints
          isSessionActive = false;

        } else {                                                                                //if user has active session and made action in webpage extend his session lifetime
          extendSession(activeSession);
        }
      }
    }


    return isSessionActive;
  }

  private String extractSessionIdFromRequest(HttpServletRequest request){
    Cookie[] cookies = request.getCookies();
    String sessionId = null;
    //System.out.println(cookies[0]);
    System.out.println(request.getPathInfo());
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
         sessionId = cookie.getValue();
        }
      }
    }


    return sessionId;
  }

  private boolean isUserSessionActive(String sessionId){
    return activeSessionRepository.existsBySessionId(sessionId);
  }

//  private boolean doesSessionNeedToBeExtended(ActiveSession activeSession){
//    Duration duration = Duration.between(activeSession.getExpirationDate(), LocalDateTime.now());
//    return duration.toSeconds() > AuthenticationController.SESSION_EXPIRATION_TIME;
//  }

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
  // TODO: make generating sessionId secure
  private String generateSessionId(){
    Random rand = new Random();

    int randomNumber = rand.nextInt(Integer.MAX_VALUE);

    return String.valueOf(randomNumber);

  }

}
