package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.ActiveSession;
import com.SecureBank.backend.entities.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveSessionRepository extends JpaRepository<ActiveSession, Long> {

  boolean existsBySessionId(String sessionId);
  boolean existsByBankUser(BankUser bankuser);

  void deleteBySessionId(String sessionId);
}
