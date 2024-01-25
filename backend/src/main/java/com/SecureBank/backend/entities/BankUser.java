package com.SecureBank.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankUser {

  @Id
  @GeneratedValue
  private long id;

  private String username;

  private byte [] password;

  @Column(columnDefinition = "bytea")
  private byte [] sessionSalt;

  @Column(columnDefinition = "bytea")
  private byte [] passwordSalt;

  @OneToOne
  @JoinColumn(name = "bankUserCredentials_id", nullable = false)
  private BankUserCredentials bankUserCredentials;

  //private boolean isLoginAlllowe;
  //private LocalDateTime loginBlockedTill;


  public BankUser(String username, byte[] password, byte[] passwordSalt,
      BankUserCredentials bankUserCredentials) {
    this.username = username;
    this.password = password;
    this.passwordSalt = passwordSalt;
    this.bankUserCredentials = bankUserCredentials;
  }
}
