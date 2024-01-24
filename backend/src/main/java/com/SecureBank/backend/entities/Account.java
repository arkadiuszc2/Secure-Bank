package com.SecureBank.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

  public Account(String number, BigDecimal balance, BankUser bankUser) {
    this.number = number;
    this.balance = balance;
    this.bankUser = bankUser;
  }

  @Id
  @GeneratedValue
  private long id;

  private String number;

  private BigDecimal balance;

  @OneToOne
  @JoinColumn(name = "bankUser_id", nullable = false)
  private BankUser bankUser;


}
