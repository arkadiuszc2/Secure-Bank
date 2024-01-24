package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.UserLoginAttemptCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginAttemptCounterRepository extends JpaRepository<UserLoginAttemptCounter, Long> {

  UserLoginAttemptCounter findByBankUserUsername(String username);
}
