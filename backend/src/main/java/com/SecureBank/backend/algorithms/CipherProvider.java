package com.SecureBank.backend.algorithms;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CipherProvider {

  private final static int IV_SIZE = 16; // in bytes

  @Value("${my-variables.key}")
  private String aesKeyBase64;


  public byte[] getCipherKey(){
    return Base64.getDecoder().decode(aesKeyBase64);
  }

  public byte[] generateRandomIV(){
    SecureRandom secureRandom = new SecureRandom();
    byte[] iv = new byte[IV_SIZE];
    secureRandom.nextBytes(iv);
    return iv;
  }

  public byte[] encryptCBC(String plaintext, byte [] iv)  throws Exception{
    byte[] keyBytes = getCipherKey();
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

    byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

    return encryptedBytes;
  }

  public String decryptCBC(byte [] ciphertext, byte [] iv) throws Exception {
    byte[] keyBytes = getCipherKey();
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

    byte[] decryptedBytes = cipher.doFinal(ciphertext);
    return new String(decryptedBytes);
  }
}
