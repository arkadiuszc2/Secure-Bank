package com.SecureBank.backend.dto;

import com.SecureBank.backend.entities.Transfer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class TransferDto {

  private String fromAccountNumber;
  private String toAccountNumber;
  private String value;
  private String date;

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public TransferDto(Transfer transfer) {
    this.fromAccountNumber = transfer.getFromAccountNumber();
    this.toAccountNumber = transfer.getToAccountNumber();
    this.value = transfer.getAmount().toString();
    this.date = transfer.getDate().format(formatter);
  }
}
