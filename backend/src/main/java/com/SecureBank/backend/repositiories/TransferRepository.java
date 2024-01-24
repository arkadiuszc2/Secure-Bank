package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.Transfer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
  List<Transfer> findAllByFromAccountNumberOrToAccountNumberOrderByDate(String fromAccount, String toAccount);
}
