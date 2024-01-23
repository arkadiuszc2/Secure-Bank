package com.SecureBank.backend.controllers;

import com.SecureBank.backend.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notauthenticated")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  public static final int SESSION_EXPIRATION_TIME = 60; //time in seconds, basic 300 s (5 min)

  public static final int USER_COOKIE_EXP_TIME = 100;    //TODO: decide what value should be set

  public static final String USERNAME_COOKIE_NAME = "bankClientUsername";

  @PostMapping("/login")
  public String login(HttpServletRequest request, HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password ){
    String sessionIdBase64Format = authenticationService.loginUserWithFullPassword(username, password, request);
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


  @PostMapping("/register")
  public String register(@RequestParam("username") String username, @RequestParam("password") String password){
    authenticationService.registerUser(username, password);
    return "Successfully registered - now log in";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request){
    authenticationService.logoutUser(request);

    return "Successfully logged out";
  }
}
