package com.SecureBank.backend.algorithms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntropyCalculator {

  public double calculateEntropy(String password) {
    int charSetSize = 0;
    boolean hasLowerCaseLetter = false;
    boolean hasUpperCaseLetter = false;
    boolean hasDigit = false;
    boolean hasSpecialChar = false;

    for (char c : password.toCharArray()) {
      if (Character.isLowerCase(c)) {
        hasLowerCaseLetter = true;
      } else if (Character.isUpperCase(c)) {
        hasUpperCaseLetter = true;
      } else if (Character.isDigit(c)) {
        hasDigit = true;
      } else {
        hasSpecialChar = true;
      }
    }

    if(hasUpperCaseLetter){
      charSetSize+=26;
    }
    if(hasLowerCaseLetter){
      charSetSize+=26;
    }
    if(hasDigit){
      charSetSize+=10;
    }
    if(hasSpecialChar){
      charSetSize+=32;
    }
    double entropy = (Math.log(charSetSize) / Math.log(2))*password.length();

    return (Math.log(charSetSize) / Math.log(2))*password.length();
  }


  public String checkEntropyCategory(double entropy){
    if (entropy < 25){
      return "very weak";
    } else if (entropy < 59){
      return "weak";
    } else if(entropy < 75){
      return "strong enough";
    } else {
      return "very strong";
    }
  }
}



