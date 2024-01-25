package com.SecureBank.backend.services;

import static com.SecureBank.backend.services.AuthenticationService.MINIMAL_PASSWORD_LENGTH;

import com.SecureBank.backend.algorithms.EntropyCalculator;
import com.SecureBank.backend.dto.AccountViewDto;
import com.SecureBank.backend.dto.TransferDto;
import com.SecureBank.backend.entities.Account;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.entities.Transfer;
import com.SecureBank.backend.entities.UserPassCharCombination;
import com.SecureBank.backend.repositiories.AccountRepository;
import com.SecureBank.backend.repositiories.BankUserRepository;
import com.SecureBank.backend.repositiories.TransferRepository;
import com.SecureBank.backend.repositiories.UserPassCharCombinationsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankUserService {

  private final BankUserRepository bankUserRepository;
  private final AuthenticationService authenticationService;
  private final UserPassCharCombinationsRepository userPassCharCombinationsRepository;
  private final PatternLoginService patternLoginService;
  private final CredentialsCipher credentialsCipher;
  private final AccountRepository accountRepository;
  private final EntropyCalculator entropyCalculator;

  @Transactional
  public String updatePassword( HttpServletRequest request, String password, String newPassword,
      String repeatPassword) {

    double newPasswordEntropy = entropyCalculator.calculateEntropy(newPassword);
    String entropyCategory = entropyCalculator.checkEntropyCategory(newPasswordEntropy);
    String infoMessage = "Provided password strength: " + entropyCategory;
    if(newPassword.length() < AuthenticationService.MINIMAL_PASSWORD_LENGTH+1){
          infoMessage += "| Update rejected! | Provided password is too short!";
    }
    else if(newPasswordEntropy < AuthenticationService.MINIMAL_PASSWORD_ENTROPY ){
      infoMessage+= ("| Update rejected! | Provide stronger password to register! You can use uppercase and lowercase letters, special characters and digits!");
    } else {
      infoMessage += "| \n Successfully updated password";

      String[] cookiesData = authenticationService.extractSessionIdAndUsernameFromRequest(request);
      String username = cookiesData[0];
      BankUser bankUser = bankUserRepository.findByUsername(username)
          .orElseThrow(() -> new NoSuchElementException("User with this username does not exist"));

      byte[] passwordHashInDb = bankUser.getPassword();
      byte[] providedPasswordByteFormat = password.getBytes();
      byte[] providedPasswordHash = authenticationService.hashData(providedPasswordByteFormat,
          bankUser.getPasswordSalt());

      if (!Arrays.equals(providedPasswordHash, passwordHashInDb)) {
        infoMessage += " | Update rejected! Provided wrong current password!";
      }
      if (!newPassword.equals(repeatPassword)) {
        infoMessage += " | Update rejected! Repeated password does not equal new password!";
      }

      bankUser.setPassword(providedPasswordHash);
      userPassCharCombinationsRepository.deleteAllByBankUserId(bankUser.getId());
      patternLoginService.generatePassCharCombinations(username, newPassword,
          bankUser.getPasswordSalt());

    }
    return infoMessage;
  }

  public AccountViewDto getAccountInfo(HttpServletRequest request){
    String [] cookiesData = authenticationService.extractSessionIdAndUsernameFromRequest(request);
    String username = cookiesData[0];

    Account account = accountRepository.findByBankUserUsername(username).orElseThrow(() -> new RuntimeException("User does not have account"));
    AccountViewDto accountViewDto = new AccountViewDto(account);
    return accountViewDto;
  }

  public String [] getCredentials(HttpServletRequest request){
    return credentialsCipher.decryptCredentials(request);
  }

}
