package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.UserPassCharCombination;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPassCharCombinationsRepository extends JpaRepository<UserPassCharCombination, Long> {
  Optional<UserPassCharCombination> findByCombinationNumber(int combinationNumber);

  UserPassCharCombination findBySelected(boolean isSelected);

  void deleteAllByBankUserId(long bankUserId);
}
