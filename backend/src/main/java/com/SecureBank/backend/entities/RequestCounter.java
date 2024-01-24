package com.SecureBank.backend.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCounter {
  @Id
  @GeneratedValue
  private long id;

  private String ipAddress;

  private int requestNumber;

  private LocalDateTime reqPeriodStartDate;
}
