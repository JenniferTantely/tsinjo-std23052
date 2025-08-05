package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Payment {
    @Id
    private String id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        VERIFYING,
        SUCCEEDED,
        FAILED
    }
}
