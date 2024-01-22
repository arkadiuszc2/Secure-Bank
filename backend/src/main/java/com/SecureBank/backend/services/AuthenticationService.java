package com.SecureBank.backend.services;

import com.SecureBank.backend.entities.ActiveSession;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.repositiories.ActiveSessionRepository;
import com.SecureBank.backend.repositiories.BankUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final ActiveSessionRepository activeSessionRepository;
  private final BankUserRepository bankUserRepository;
  private static final String SESSION_COOKIE_NAME = "sessionId";
  public boolean isUserSessionActive(String sessionId){
    return activeSessionRepository.existsBySessionId(sessionId);
  }

  public void registerUser(String username, String password){
    //TODO: check if user with this username already exists
    BankUser newBankUser = new BankUser(0, username, password);
    bankUserRepository.save(newBankUser);
  }

  public String loginUser(String username, String password){
    BankUser bankUser = bankUserRepository.findByUsername(username).orElseThrow( () -> new NoSuchElementException("User with this username does not exist"));
    String passwordIndDB = bankUser.getPassword();
    String sessionId = null;

    if(!password.equals(passwordIndDB)){
      throw new RuntimeException("Wrong password");
    }

    if(!activeSessionRepository.existsByBankUser(bankUser)){
      sessionId = generateSessionId();
      activeSessionRepository.save(new ActiveSession(0, sessionId, bankUser));
    } else {
      throw new RuntimeException("Already logged-in");
    }


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

  public boolean checkIfUserAuthenticated(HttpServletRequest request){
    boolean isSessionValid = false;

    String sessionId = extractSessionIdFromRequest(request);
    System.out.println(sessionId);
    if(sessionId == null){
      throw new RuntimeException("User is not logged in or his sessionId expired");
    } else {
      isSessionValid = isUserSessionActive(sessionId);
    }

    return isSessionValid;
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

  //temporary implementation for tests
  // TODO: make generating sessionId secure
  private String generateSessionId(){
    Random rand = new Random();

    int randomNumber = rand.nextInt(Integer.MAX_VALUE);

    return String.valueOf(randomNumber);

  }

}
