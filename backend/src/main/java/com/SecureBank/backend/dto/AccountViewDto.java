package com.SecureBank.backend.dto;

import com.SecureBank.backend.entities.Account;
import lombok.Data;

@Data
public class AccountViewDto {
  private String number;
  private String balance;

  public AccountViewDto(Account account) {
    this.number = account.getNumber();
    this.balance = account.getBalance().toString();
  }
}
