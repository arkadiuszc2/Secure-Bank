package com.SecureBank.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

  public Account(String accountNumber, BigDecimal balance, BankUser bankUser) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.bankUser = bankUser;
  }

  @Id
  @GeneratedValue
  private long id;

  private String accountNumber;

  private BigDecimal balance;

  @OneToOne
  @JoinColumn(name = "bankUser_id", nullable = false)
  private BankUser bankUser;


}
