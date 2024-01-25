package com.SecureBank.backend.services;

import com.SecureBank.backend.algorithms.CipherProvider;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.entities.BankUserCredentials;
import com.SecureBank.backend.repositiories.BankUserCredentialsRepository;
import com.SecureBank.backend.repositiories.BankUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialsCipher {

  private final BankUserCredentialsRepository bankUserCredentialsRepository;
  private final CipherProvider cipherProvider;
  private final BankUserRepository bankUserRepository;

  public BankUserCredentials encryptCredentials(String name, String surname, String identificationNumber){
    String [] dataToEncrypt = {name, surname, identificationNumber};
    byte [][] encryptedData = new byte[dataToEncrypt.length][];
    byte [][] iv = new byte[dataToEncrypt.length][];

    for (int i = 0; i < dataToEncrypt.length; i++){
      iv[i] = cipherProvider.generateRandomIV();
      try {
        encryptedData[i] = cipherProvider.encryptCBC(dataToEncrypt[i], iv[i]);
      } catch (Exception e) {
        throw new RuntimeException("Error while encrypting sensitive data!");
      }

    }

    BankUserCredentials bankUserCredentials = new BankUserCredentials(encryptedData[0], encryptedData[1], encryptedData[2], iv[0], iv[1], iv[2]);
    bankUserCredentialsRepository.save(bankUserCredentials);

    return bankUserCredentials;


  }

  public String [] decryptCredentials(HttpServletRequest request){
    String [] cookiesData = AuthenticationService.extractSessionIdAndUsernameFromRequest(request);
    String username = cookiesData[0];
    int dataNumber = 3;
    String [] decryptedData = new String[dataNumber];
    BankUser bankUser = bankUserRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Error while getting bank user!"));
    BankUserCredentials bankUserCredentials = bankUser.getBankUserCredentials();
    byte [] iv;
    byte [] dataToDecrypt;

    for (int i = 0; i < dataNumber; i++){

      if( i == 0){
        iv = bankUserCredentials.getIvName();
        dataToDecrypt = bankUserCredentials.getEncryptedName();
      } else if( i == 1){
        iv = bankUserCredentials.getIvSurname();
        dataToDecrypt = bankUserCredentials.getEncryptedSurname();
      } else {
        iv = bankUserCredentials.getIvIdentificationNumber();
        dataToDecrypt = bankUserCredentials.getEncryptedIdentificationNumber();
      }

      try {
        decryptedData[i] = cipherProvider.decryptCBC(dataToDecrypt, iv);
      } catch (Exception e) {
        throw new RuntimeException("Error while decrypting sensitive data! " + e.getMessage());
      }

    }

    return decryptedData;


  }

}
