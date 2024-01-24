package com.SecureBank.backend.services;

import com.SecureBank.backend.algorithms.CipherProvider;
import com.SecureBank.backend.entities.BankUserCredentials;
import com.SecureBank.backend.repositiories.BankUserCredentialsRepostitory;
import com.SecureBank.backend.repositiories.BankUserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialsEncryptor {

  private final BankUserCredentialsRepostitory bankUserCredentialsRepostitory;
  private final CipherProvider cipherProvider;

  public BankUserCredentials encryptCredentials(String name, String surname, String identificationNumber){
    String [] dataToEncrypt = {name, surname, identificationNumber};
    byte [][] encryptedData = new byte[dataToEncrypt.length][];
    byte [][] iv = new byte[dataToEncrypt.length][];

    for (int i = 0; i < dataToEncrypt.length; i++){
      iv[i] = cipherProvider.generateRandomIV();
      try {
        encryptedData[i] = cipherProvider.encryptCBC(name, iv[i]);
      } catch (Exception e) {
        throw new RuntimeException("Error while encrypting sensitive data!");
      }

    }

    BankUserCredentials bankUserCredentials = new BankUserCredentials(encryptedData[0], encryptedData[1], encryptedData[2], iv[0], iv[1], iv[2]);
    bankUserCredentialsRepostitory.save(bankUserCredentials);

    return bankUserCredentials;


  }

}
