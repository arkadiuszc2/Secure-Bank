package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.ActiveSession;
import com.SecureBank.backend.entities.BankUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveSessionRepository extends JpaRepository<ActiveSession, Long> {

  boolean existsBySessionId(String sessionId);
  boolean existsByBankUser(BankUser bankuser);

  void deleteByBankUser(BankUser bankUser);
  void deleteBySessionId(String sessionId);

  Optional<ActiveSession> findBySessionId(String sessionId);

  ActiveSession findByBankUser(BankUser bankUser);
}
