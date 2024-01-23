package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.BankUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankUserRepository extends JpaRepository<BankUser, Long> {
  Optional<BankUser> findByUsername(String username);
  boolean existsByUsername(String username);
}
