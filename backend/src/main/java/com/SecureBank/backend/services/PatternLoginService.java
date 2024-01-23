package com.SecureBank.backend.services;

import com.SecureBank.backend.cipher.CipherProvider;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.entities.UserPassCharCombination;
import com.SecureBank.backend.repositiories.BankUserRepository;
import com.SecureBank.backend.repositiories.UserPassCharCombinationsRepository;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatternLoginService {

  private final BankUserRepository bankUserRepository;
  private final UserPassCharCombinationsRepository userPassCharCombinationsRepository;
  private final static int COMBINATION_LENGTH = 8;
  private final static int COMBINATIONS_NUMBER = 8;

  private final CipherProvider cipherProvider;

  public void generatePassCharCombinations(String username, String password, byte[] passwordSalt) {
    BankUser bankUser = bankUserRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Account with provided username does not exist"));

    SecureRandom random = new SecureRandom();



    int passwordLength = password.length();

    byte [] AesKey = cipherProvider.getCipherKey();


    for (int i = 0; i < COMBINATIONS_NUMBER; i++) {
      Set<Integer> usedIndexes = new HashSet<>();

      StringBuilder combination= new StringBuilder();
      StringBuilder charNumbers= new StringBuilder();

      while (usedIndexes.size() < COMBINATION_LENGTH) {
        int randomIndex = random.nextInt(passwordLength);
        int characterPositionInPass = randomIndex+1; //for user becouse without it it start from 0
        if(!usedIndexes.contains(randomIndex)){
          charNumbers.append(characterPositionInPass).append("|");
          combination.append(password.charAt(randomIndex));
        }
        usedIndexes.add(randomIndex);
      }

      System.out.println(charNumbers);



      byte[] hashedCombination = AuthenticationService.hashData(combination.toString().getBytes(
          StandardCharsets.UTF_8), passwordSalt);

      byte [] encryptedCharNumbers;
      byte [] iv = cipherProvider.generateRandomIV();

      try {
        encryptedCharNumbers =cipherProvider.encryptCBC(charNumbers.toString(),iv);
      } catch (Exception e) {
        throw new RuntimeException("Encryption failed");
      }

      userPassCharCombinationsRepository.save(new UserPassCharCombination(0, bankUser, i, iv, encryptedCharNumbers, hashedCombination, false ));
    }
  }

  public String getCharacterNumbers() throws Exception {
    SecureRandom secureRandom = new SecureRandom();
    int combinationNumber = secureRandom.nextInt(COMBINATIONS_NUMBER);
    UserPassCharCombination userPassCharCombination  = userPassCharCombinationsRepository.findByCombinationNumber(combinationNumber).orElseThrow();


    byte [] charactersNumbersEncrypted = userPassCharCombination.getCharactersNumbers();
    byte [] iv = userPassCharCombination.getIv();

    String characterNumbersDecrypted = cipherProvider.decryptCBC(charactersNumbersEncrypted, iv);

    UserPassCharCombination oldSelectedCombination = userPassCharCombinationsRepository.findBySelected(true);
    if(oldSelectedCombination!=null){
      oldSelectedCombination.setSelected(false);
      userPassCharCombinationsRepository.save(oldSelectedCombination);
    }

    userPassCharCombination.setSelected(true);
    userPassCharCombinationsRepository.save(userPassCharCombination);

    return characterNumbersDecrypted;
  }

}
