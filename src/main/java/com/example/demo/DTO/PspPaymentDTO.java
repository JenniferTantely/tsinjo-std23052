package com.example.demo.DTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PspPaymentDTO {
  private String pspType;
  private String id;
  private Integer amount;
  private String creationInstant;
}
