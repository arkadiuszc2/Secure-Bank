package com.SecureBank.backend.repositiories;

import com.SecureBank.backend.entities.UserPassCharCombination;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPassCharCombinationsRepository extends JpaRepository<UserPassCharCombination, Long> {
  Optional<UserPassCharCombination> findByCombinationNumberAndBankUser_Username(int combinationNumber, String username);

  UserPassCharCombination findBySelectedAndBankUser_Username(boolean isSelected, String username);

  @Query("SELECT u FROM UserPassCharCombination u WHERE u.bankUser.username = :username ORDER BY RAND()")
  Optional<UserPassCharCombination> findRandomCombinationForUsername(@Param("username") String username);
  void deleteAllByBankUserId(long bankUserId);
}
