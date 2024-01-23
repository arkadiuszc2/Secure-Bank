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

  @PostMapping("/login")
  public String login(HttpServletRequest request, HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password ){
    String sessionId = authenticationService.loginUser(username, password, request);
    Cookie cookie = new Cookie(AuthenticationService.SESSION_COOKIE_NAME, sessionId);
    cookie.setPath("/");
    cookie.setMaxAge(10);  //cookie is saved in web client for 300 s, if user accidentally close the page with bank cookie will still be remembered for this time
    response.addCookie(cookie);
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
