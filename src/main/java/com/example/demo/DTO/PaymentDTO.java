package com.example.demo.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
  private String id;
  private PspPaymentDTO pspPayment;
  private String creationInstant;
  private String lastPspVerificationInstant;
  private Integer verificationAttemptNb;
  private UserDTO payer;
  private ApplicationDTO application;
  private String verificationStatus;
}
