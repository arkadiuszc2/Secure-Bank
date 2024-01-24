package com.SecureBank.backend.services;

import com.SecureBank.backend.entities.Account;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.repositiories.AccountRepository;
import java.math.BigDecimal;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCreator {

  private final AccountRepository accountRepository;
  private final int ACCOUNT_NUMBER_LENGTH = 22;
  public void createNewAccount(BankUser bankUser){
    long seed = calculateSeed(bankUser.getUsername());
    Random random = new Random(seed);
    StringBuilder accountNumberBuilder = new StringBuilder();

    do {
      for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
        accountNumberBuilder.append(random.nextInt(10));
      }
    } while (accountRepository.existsByAccountNumber(accountNumberBuilder.toString()));

    String accountNumber = accountNumberBuilder.toString();

    Account account = new Account(accountNumber, new BigDecimal(0), bankUser);
    accountRepository.save(account);
    }






  private static long calculateSeed(String username) {
    long seed = 0;
    for (char c : username.toCharArray()) {
      seed += (int) c;
    }
    return seed;
  }

}
