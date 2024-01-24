package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.RequestCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestCounterRepository extends JpaRepository<RequestCounter, Long> {
  RequestCounter findByIpAddress(String ipAddress);
}
