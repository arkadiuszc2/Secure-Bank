package com.SecureBank.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginAttemptCounter {

  @Id
  @GeneratedValue
  private long id;
  @OneToOne
  @JoinColumn(name = "bankUser_id", nullable = false)
  private BankUser bankUser;
  private LocalDateTime firstAttemptDate;
  private int loginAttempts;  //how many login attempts from firstAttemptDate to LocalDateTime.now(), if too much ex 10 i set bankUser.isAccessible to false
                              // and i will reset this after ex 10 min
  private boolean blockUserTemporarily;

  //private LocalDateTime firstSuspiciousTrafficDate;
  //private int suspiciousTrafficNumber;   // count situations when max login attempts where exceeded - if 1 set timeout t
                                          // If not much time elapsed from firstSuspiciousTrafficDate to now increment susTrafficNumber what will lengthen
                                          //isAccessible = false time (maybe to the point when account is blocked)
}
