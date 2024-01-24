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
public class BankUserCredentials {
  @Id
  @GeneratedValue
  private long id;
  @Lob
  private byte [] encryptedName;
  @Lob
  private byte [] encryptedSurname;
  @Lob
  private byte[] encryptedIdentificationNumber;
  @Lob
  private byte [] ivName;
  @Lob
  private byte [] ivSurname;
  @Lob
  private byte [] ivIdentificationNumber;


  public BankUserCredentials(byte[] encryptedName, byte[] encryptedSurname,
      byte[] encryptedIdentificationNumber, byte[] ivName, byte[] ivSurname,
      byte[] ivIdentificationNumber) {
    this.encryptedName = encryptedName;
    this.encryptedSurname = encryptedSurname;
    this.encryptedIdentificationNumber = encryptedIdentificationNumber;
    this.ivName = ivName;
    this.ivSurname = ivSurname;
    this.ivIdentificationNumber = ivIdentificationNumber;
  }
}
