package com.example.demo.service;


import com.example.demo.DTO.PaymentDTO;
import com.example.demo.DTO.PspPaymentDTO;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentVerificationSchedulerTest {
    @Test
    void shouldUpdatePaymentStatusWhenSucceededFromVola() {
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        PaymentVerificationScheduler scheduler = new PaymentVerificationScheduler(paymentRepository, restTemplate);

        Payment verifyingPayment = Payment.builder()
                .id("123")
                .amount(BigDecimal.valueOf(1000))
                .status(Payment.Status.VERIFYING)
                .payerEmail("test@example.com")
                .pspType("ORANGE_MONEY")
                .build();

        when(paymentRepository.findByStatus(Payment.Status.VERIFYING))
                .thenReturn(List.of(verifyingPayment));

        PaymentDTO volaResponse = new PaymentDTO();
        volaResponse.setId("123");
        volaResponse.setVerificationStatus("SUCCEEDED");
        volaResponse.setPspPayment(new PspPaymentDTO("ORANGE_MONEY", "abc", 1000, "2025-08-05T10:00:00"));

        when(restTemplate.getForObject(anyString(), eq(PaymentDTO.class)))
                .thenReturn(volaResponse);

        scheduler.verifyPayments();

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());

        Payment savedPayment = captor.getValue();

        assertThat(savedPayment.getStatus()).isEqualTo(Payment.Status.SUCCEEDED);
    }
}