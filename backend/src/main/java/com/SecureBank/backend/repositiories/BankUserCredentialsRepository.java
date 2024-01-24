package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.BankUserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankUserCredentialsRepository extends JpaRepository<BankUserCredentials, Long> {

}
