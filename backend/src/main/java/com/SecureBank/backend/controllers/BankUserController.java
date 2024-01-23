package com.SecureBank.backend.controllers;

import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.services.BankUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticated/bankUser")
@RequiredArgsConstructor
public class BankUserController {

  private final BankUserService bankUserService;

  @PutMapping
  public void updatePassword(HttpServletRequest request, String password, @RequestParam("newPassword") String newPassword, @RequestParam("repPassword") String repeatPassword){
    bankUserService.updatePassword(request, password, newPassword, repeatPassword);
  }

}
