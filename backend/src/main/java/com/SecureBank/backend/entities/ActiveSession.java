package com.SecureBank.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
  @Column(columnDefinition = "bytea")
  private byte [] sessionIdHashed;
  @OneToOne
  @JoinColumn(name = "bankUser_id", nullable = false)
  private BankUser bankUser;
  private LocalDateTime expirationDate;
  private LocalDateTime createdAt;
}
