package com.example.demo.DTO;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PspPaymentDTO {
    private String pspType;
    private String id;
    private Integer amount;
    private String creationInstant;
}
