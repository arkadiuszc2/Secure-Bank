package com.SecureBank.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveSession {

  @Id
  @GeneratedValue
  private long id;
  private String sessionId;
  @OneToOne
  @JoinColumn(name = "client_id", nullable = false)
  private BankUser bankUser;
  private LocalDateTime expirationDate;
  private LocalDateTime createdAt;
}
