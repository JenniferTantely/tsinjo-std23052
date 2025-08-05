package com.example.demo.service;

import com.example.demo.DTO.PaymentDTO;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentVerificationScheduler {
  private final PaymentRepository paymentRepository;
  private final RestTemplate restTemplate = new RestTemplate();

  private final String volaApiBase =
      "https://42cwka3n4ifcp7ufheyrpmph240iuaxo.lambda-url.eu-west-3.on.aws";

  @Value("${API_KEY}")
  private String apiKey;

  @Scheduled(fixedRate = 30000) // toutes les 30 sec
  public void verifyPayments() {
    log.info("Vérification des paiements en cours...");

    List<Payment> verifyingPayments = paymentRepository.findByStatus(Payment.Status.VERIFYING);

    for (Payment payment : verifyingPayments) {
      try {
        String url =
            volaApiBase
                + "/payment?apiKey="
                + apiKey
                + "&payerEmail="
                + payment.getPayerEmail()
                + "&pspType="
                + payment.getPspType()
                + "&pspPaymentId="
                + payment.getId();

        PaymentDTO paymentDTO = restTemplate.getForObject(url, PaymentDTO.class);

        if (paymentDTO != null) {
          payment.setStatus(Payment.Status.valueOf(paymentDTO.getVerificationStatus()));
          paymentRepository.save(payment);

          log.info("Paiement {} mis à jour en {}", payment.getId(), payment.getStatus());
        }
      } catch (Exception e) {
        log.error("Erreur vérification paiement {}", payment.getId(), e);
      }
    }
  }
}
