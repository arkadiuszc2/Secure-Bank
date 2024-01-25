package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByAccountNumber(String number);

  Optional<Account> findByBankUserUsername(String username);

  @Query("SELECT a FROM Account a WHERE a.accountNumber = ?1")
  Optional<Account> findByAccountNumberCustom(String accountNumber);

  Optional<Account> findByAccountNumber(String accountNumber);
}
