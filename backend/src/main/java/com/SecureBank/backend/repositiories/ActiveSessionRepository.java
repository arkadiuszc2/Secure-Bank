package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.ActiveSession;
import com.SecureBank.backend.entities.BankUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveSessionRepository extends JpaRepository<ActiveSession, Long> {

  boolean existsBySessionIdHashedAndBankUserUsername(byte[] sessionIdHashed, String username);
  boolean existsByBankUser(BankUser bankuser);

  void deleteByBankUser(BankUser bankUser);
  void deleteBySessionIdHashed(byte[] sessionIdHashed);

  void deleteByBankUserUsername(String username);

  Optional<ActiveSession> findBySessionIdHashed(byte [] sessionIdHashed);

  ActiveSession findByBankUser(BankUser bankUser);

  Optional<ActiveSession> findByBankUserUsername(String username);
}
