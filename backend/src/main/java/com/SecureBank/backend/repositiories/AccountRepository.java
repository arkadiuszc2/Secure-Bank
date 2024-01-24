package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByNumber(String number);

  Optional<Account> findByBankUserUsername(String username);
}
