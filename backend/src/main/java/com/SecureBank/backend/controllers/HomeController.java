package com.SecureBank.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticated")
public class HomeController {

  @GetMapping("/home")
  public String home(){
    System.out.println("MESSAGE: ");
    return "home";
  }

}
