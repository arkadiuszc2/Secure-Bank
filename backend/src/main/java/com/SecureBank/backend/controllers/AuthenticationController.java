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
@RequestMapping("/unauthenticated")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final PatternLoginService patternLoginService;
  public static final int SESSION_EXPIRATION_TIME = 60; //time in seconds, basic 300 s (5 min)

  public static final int USER_COOKIE_EXP_TIME = 100;    //TODO: decide what value should be set

  public static final String USERNAME_COOKIE_NAME = "bankClientUsername";

  @PostMapping("/login")
  public String login(HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password ){
    String sessionIdBase64Format = authenticationService.login(username, password, false);
    Cookie sessionIdCookie = new Cookie(AuthenticationService.SESSION_COOKIE_NAME, sessionIdBase64Format);
    sessionIdCookie.setPath("/");
    sessionIdCookie.setMaxAge(USER_COOKIE_EXP_TIME);  //cookie is saved in web client for 300 s, if user accidentally close the page with bank cookie will still be remembered for this time
    response.addCookie(sessionIdCookie);

    Cookie usernameCookie = new Cookie(USERNAME_COOKIE_NAME, username);
    usernameCookie.setPath("/");
    usernameCookie.setMaxAge(USER_COOKIE_EXP_TIME);
    response.addCookie(usernameCookie);

    return "Successfully logged in - sessionId assigned";
  }

  @PostMapping("requestPartialPassLogin/{username}")
  public String requestPartialPassLogin(@PathVariable("username") String username)
      throws Exception {
    return patternLoginService.getCharacterNumbers();
  }

  @PostMapping("/partialPassLogin")
  public String partialPassLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
    authenticationService.login(username, password, true);
    return "Successfully logged in - sessionId assigned";
  }

  @PostMapping("/register")
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
