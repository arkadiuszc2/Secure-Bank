package com.SecureBank.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
  @Lob
  private byte [] sessionSalt;

  @Lob
  private byte [] passwordSalt;

}
