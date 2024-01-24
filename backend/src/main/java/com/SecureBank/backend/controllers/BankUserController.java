package com.SecureBank.backend.controllers;

import com.SecureBank.backend.dto.AccountViewDto;
import com.SecureBank.backend.dto.TransferDto;
import com.SecureBank.backend.entities.Account;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.services.BankUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticated/bankUser")
@RequiredArgsConstructor
public class BankUserController {

  private final BankUserService bankUserService;

  @PutMapping("/updatePassword")
  public void updatePassword(HttpServletRequest request, String password, @RequestParam("newPassword") String newPassword, @RequestParam("repPassword") String repeatPassword){
    bankUserService.updatePassword(request, password, newPassword, repeatPassword);
  }

  @GetMapping("/showCredentials")
  public String [] getCredentials(HttpServletRequest request){
    return bankUserService.getCredentials(request);
  }

  @GetMapping("/accountInfo")
  public AccountViewDto getAccountInfo(HttpServletRequest request){
    return bankUserService.getAccountInfo(request);
  }

}
