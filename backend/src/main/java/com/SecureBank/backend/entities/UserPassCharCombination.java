package com.SecureBank.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPassCharCombination {

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne
  @JoinColumn(name = "bankUser_id", nullable = false)
  private BankUser bankUser;

  private int combinationNumber;

  private byte [] iv;
  @Column(columnDefinition = "bytea")
  private byte[] charactersNumbers;

  @Column(columnDefinition = "bytea")
  private byte [] combinationHash;

  private boolean selected;

}
