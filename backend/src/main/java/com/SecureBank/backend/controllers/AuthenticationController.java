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

  @PostMapping("/login")
  public String login(HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password ){
    String sessionId = authenticationService.loginUser(username, password);
    Cookie cookie = new Cookie("sessionId", sessionId);
    cookie.setPath("/");
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
