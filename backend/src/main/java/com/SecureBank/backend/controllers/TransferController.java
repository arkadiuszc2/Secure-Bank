package com.SecureBank.backend.controllers;

import com.SecureBank.backend.dto.TransferDto;
import com.SecureBank.backend.services.BankUserService;
import com.SecureBank.backend.services.TransferService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticated")
@RequiredArgsConstructor
public class TransferController {

  private final TransferService transferService;

  @GetMapping("/transfers")
  public List<TransferDto> getTransfers(HttpServletRequest request){
    return transferService.getTransfers(request);
  }

  @PostMapping("/sendTransfer")
  public void sendTransfer(HttpServletRequest request, @RequestParam("destAccountNum") String destinationAccountNumber, @RequestParam("value") String value){
    transferService.sendTransfer(request, destinationAccountNumber, value);
  }

}
