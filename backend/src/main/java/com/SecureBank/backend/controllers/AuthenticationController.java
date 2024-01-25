package com.SecureBank.backend.controllers;

import com.SecureBank.backend.services.AuthenticationService;
import com.SecureBank.backend.services.PatternLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/unauthenticated")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final PatternLoginService patternLoginService;
  public static final int SESSION_EXPIRATION_TIME = 300;

  public static final int USER_COOKIE_EXP_TIME = 300;


  @GetMapping("/login")
  public String login(HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password ){
    String sessionIdBase64Format = authenticationService.login(username, password, true);
    String cookieText = username + "-" + sessionIdBase64Format;
    Cookie sessionIdCookie = new Cookie(AuthenticationService.SESSION_COOKIE_NAME, cookieText);
    sessionIdCookie.setMaxAge(USER_COOKIE_EXP_TIME);
    sessionIdCookie.setHttpOnly(true);
    sessionIdCookie.setPath("/");
    sessionIdCookie.setSecure(true);
    response.addCookie(sessionIdCookie);

    return "Successfully logged in - sessionId assigned";
  }

  @PostMapping("/requestPartialPassLogin/{username}")
  public String requestPartialPassLogin(@PathVariable("username") String username)
      throws Exception {
    return patternLoginService.getCharacterNumbers(username);
  }

  @GetMapping("/partialPassLogin")
  public String partialPassLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) {
    String sessionIdBase64Format = authenticationService.login(username, password, true);
    String cookieText = username + "-" + sessionIdBase64Format;
    Cookie sessionIdCookie = new Cookie(AuthenticationService.SESSION_COOKIE_NAME, cookieText);
    sessionIdCookie.setMaxAge(USER_COOKIE_EXP_TIME);
    sessionIdCookie.setHttpOnly(true);
    sessionIdCookie.setPath("/");
    sessionIdCookie.setSecure(true);
    response.addCookie(sessionIdCookie);
    return "Successfully logged in - sessionId assigned";
  }

  @GetMapping("/register")
  public String register(@RequestParam("username") String username, @RequestParam("password") String password,
      @RequestParam ("fullName") String name, @RequestParam ("surname") String surname, @RequestParam ("identificationNumber") String identificationNumber){
    String infoMessage = authenticationService.registerUser(username, password, name, surname, identificationNumber);
    return infoMessage;
  }

  @PostMapping("/logout")
  public String logout(HttpServletRequest request){
    authenticationService.logoutUser(request);

    return "Successfully logged out";
  }


}
