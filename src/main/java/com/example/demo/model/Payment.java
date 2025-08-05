package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
  @Id private String id;

  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private Status status;

  public enum Status {
    VERIFYING,
    SUCCEEDED,
    FAILED
  }
}
