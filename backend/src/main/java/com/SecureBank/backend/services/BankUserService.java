package com.SecureBank.backend.services;

import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.entities.UserPassCharCombination;
import com.SecureBank.backend.repositiories.BankUserRepository;
import com.SecureBank.backend.repositiories.UserPassCharCombinationsRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankUserService {

  private final BankUserRepository bankUserRepository;
  private final AuthenticationService authenticationService;
  private final UserPassCharCombinationsRepository userPassCharCombinationsRepository;
  private final PatternLoginService patternLoginService;

  public void updatePassword( HttpServletRequest request, String password, String newPassword,
      String repeatPassword) {
    String [] cookiesData = authenticationService.extractSessionIdAndUsernameFromRequest(request);
    String username = cookiesData[0];
    BankUser bankUser = bankUserRepository.findByUsername(username)
        .orElseThrow(() -> new NoSuchElementException("User with this username does not exist"));

    byte[] passwordHashInDb = bankUser.getPassword();
    byte[] providedPasswordByteFormat = password.getBytes();
    byte[] providedPasswordHash = authenticationService.hashData(providedPasswordByteFormat, bankUser.getPasswordSalt());

    if (!Arrays.equals(providedPasswordHash, passwordHashInDb)) {
      throw new RuntimeException("Wrong password!");
    }
    if (!newPassword.equals(repeatPassword)){
      throw new RuntimeException("Repeated password is not the same");
    }

    bankUser.setPassword(providedPasswordHash);
    userPassCharCombinationsRepository.deleteAllByBankUserId(bankUser.getId());
    patternLoginService.generatePassCharCombinations(username, newPassword, bankUser.getPasswordSalt());

  }
}
