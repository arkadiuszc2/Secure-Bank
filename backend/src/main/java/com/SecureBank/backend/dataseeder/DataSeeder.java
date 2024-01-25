package com.SecureBank.backend.dataseeder;

import com.SecureBank.backend.entities.Account;
import com.SecureBank.backend.repositiories.AccountRepository;
import com.SecureBank.backend.services.AuthenticationService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AuthenticationService authenticationService;


  @Override
  public void run(String... args) throws Exception {

    authenticationService.registerUser("user1", "passrd1$ee321321", "Jan", "Kowalski", "123 456 789");
    authenticationService.registerUser("user2", "paasdard1$ee21211", "Krzysztof", "Jurkowski", "021 212 311");

    Account user1Account = accountRepository.findByBankUserUsername("user1").orElseThrow(()-> new RuntimeException("Data seeder ex"));
    user1Account.setBalance(new BigDecimal(300));
    accountRepository.save(user1Account);

    Account user2Account = accountRepository.findByBankUserUsername("user2").orElseThrow(()-> new RuntimeException("Data seeder ex"));
    user2Account.setBalance(new BigDecimal(200));
    accountRepository.save(user2Account);
  }
}