package com.SecureBank.backend.services;

import com.SecureBank.backend.dto.TransferDto;
import com.SecureBank.backend.entities.Account;
import com.SecureBank.backend.entities.BankUser;
import com.SecureBank.backend.entities.Transfer;
import com.SecureBank.backend.repositiories.AccountRepository;
import com.SecureBank.backend.repositiories.TransferRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferService {

  private final AccountRepository accountRepository;
  private final TransferRepository transferRepository;
  private final AuthenticationService authenticationService;
  public List<TransferDto> getTransfers(HttpServletRequest request){
    String [] cookiesData = authenticationService.extractSessionIdAndUsernameFromRequest(request);
    String username = cookiesData[0];

    Account account = accountRepository.findByBankUserUsername(username).orElseThrow(() -> new RuntimeException("Account not found"));
    String accountNumber = account.getAccountNumber();
    List<Transfer> transferList = transferRepository.findAllByFromAccountNumberOrToAccountNumberOrderByDate(accountNumber, accountNumber);

    return transferList.stream()
        .map(TransferDto::new)
        .collect(Collectors.toList());

  }


  public void sendTransfer(HttpServletRequest request, String destAccountNumber, String value){
    String [] cookiesData = authenticationService.extractSessionIdAndUsernameFromRequest(request);
    String username = cookiesData[0];

    Account senderAccount = accountRepository.findByBankUserUsername(username).orElseThrow(()->new RuntimeException("User dooes not have account!"));
    Account receiverAccount = accountRepository.findByAccountNumber(destAccountNumber).orElseThrow(()->new RuntimeException("Receiver account does not exist!"));

    if(senderAccount.getBalance().compareTo(new BigDecimal(value)) < 0){
      throw new RuntimeException("You don't have enough money in your account!");
    }
    if(receiverAccount.getBankUser().getUsername().equals(username)){
      throw new RuntimeException("You cannot send money to yourself!");
    }

    Transfer transfer = new Transfer(senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), new BigDecimal(value),
        LocalDateTime.now());

    senderAccount.setBalance(senderAccount.getBalance().subtract(new BigDecimal(value)));
    receiverAccount.setBalance(receiverAccount.getBalance().add(new BigDecimal(value)));

    accountRepository.save(senderAccount);
    accountRepository.save(receiverAccount);

    transferRepository.save(transfer);
  }


}
