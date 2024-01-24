package com.SecureBank.backend.services;

import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.entities.UserLoginAttemptCounter;
import com.SecureBank.backend.repositiories.BankUserRepository;
import com.SecureBank.backend.repositiories.UserLoginAttemptCounterRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SameUserLoginService {

  private final UserLoginAttemptCounterRepository userLoginAttemptCounterRepository;
  private final BankUserRepository bankUserRepository;
  public final int MAX_LOGIN_ATTEMPTS_IN_TIME_PERIOD = 5;
  public final int LOGIN_TIME_PERIOD = 600; //in seconds
  public boolean isLoginWithThisUsernameAllowed(String username){
    UserLoginAttemptCounter userLoginAttemptCounter = userLoginAttemptCounterRepository.findByBankUserUsername(username);
    return userLoginAttemptCounter.getLoginAttempts() <= MAX_LOGIN_ATTEMPTS_IN_TIME_PERIOD;
  }

  public void saveLoginAttempt(String username){
    UserLoginAttemptCounter userLoginAttemptCounter = userLoginAttemptCounterRepository.findByBankUserUsername(username);
    BankUser bankUser = bankUserRepository.findByUsername(username).orElse(null);  //null will not happen, it is checked in parent method

    if(userLoginAttemptCounter==null){
      userLoginAttemptCounter = new UserLoginAttemptCounter(0, bankUser, LocalDateTime.now(), 1, false);
    } else {

      if (Duration.between(userLoginAttemptCounter.getFirstAttemptDate(), LocalDateTime.now())
          .toSeconds() > LOGIN_TIME_PERIOD) {
        userLoginAttemptCounter.setFirstAttemptDate(LocalDateTime.now());
        userLoginAttemptCounter.setLoginAttempts(1);

      } else {
        userLoginAttemptCounter.setLoginAttempts(userLoginAttemptCounter.getLoginAttempts() + 1);
      }
    }



    userLoginAttemptCounterRepository.save(userLoginAttemptCounter);
  }

}
