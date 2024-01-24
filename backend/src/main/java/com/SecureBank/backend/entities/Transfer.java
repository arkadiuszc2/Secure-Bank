package com.SecureBank.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {

  @Id
  @GeneratedValue
  private long id;
  private String fromAccountNumber;
  private String toAccountNumber;
  private BigDecimal amount;
  private LocalDateTime date;

  public Transfer(String fromAccountNumber, String toAccountNumber, BigDecimal value,
      LocalDateTime date) {
    this.fromAccountNumber = fromAccountNumber;
    this.toAccountNumber = toAccountNumber;
    this.amount = value;
    this.date = date;
  }
}
