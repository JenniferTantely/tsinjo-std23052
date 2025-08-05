package com.example.demo.endpoint.rest.controller;

import com.example.demo.DTO.PaymentDTO;
import com.example.demo.model.Donation;
import com.example.demo.model.Donor;
import com.example.demo.model.Payment;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.DonorRepository;
import com.example.demo.repository.PaymentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donations")
public class DonationController {
  private final DonationRepository donationRepository;
  private final DonorRepository donorRepository;
  private final PaymentRepository paymentRepository;
  private final RestTemplate restTemplate = new RestTemplate();

  private final String volaApiBase =
      "https://42cwka3n4ifcp7ufheyrpmph240iuaxo.lambda-url.eu-west-3.on.aws";

  @Value("${API_KEY}")
  private String apiKey;

  @PostMapping
  public ResponseEntity<String> createDonation(
      @RequestParam String donorEmail,
      @RequestParam String donorName,
      @RequestParam Double amount,
      @RequestParam String pspPaymentId) {

    Donor donor =
        donorRepository
            .findByEmail(donorEmail)
            .orElseGet(
                () -> {
                  Donor d = new Donor();
                  d.setEmail(donorEmail);
                  d.setFullName(donorName);
                  return donorRepository.save(d);
                });

    String url =
        volaApiBase
            + "/payment?apiKey="
            + apiKey
            + "&payerEmail="
            + donorEmail
            + "&pspType=ORANGE_MONEY"
            + "&pspPaymentId="
            + pspPaymentId;

    PaymentDTO paymentDTO = restTemplate.postForObject(url, null, PaymentDTO.class);

    Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    payment.setAmount(BigDecimal.valueOf(paymentDTO.getPspPayment().getAmount()));
    payment.setStatus(Payment.Status.VERIFYING);
    payment.setPayerEmail(donorEmail);
    payment.setPspType(paymentDTO.getPspPayment().getPspType());
    paymentRepository.save(payment);

    Donation donation = new Donation();
    donation.setAmount(BigDecimal.valueOf(amount));
    donation.setDate(LocalDateTime.now());
    donation.setDonor(donor);
    donation.setPayment(payment);
    donationRepository.save(donation);

    return ResponseEntity.ok("Donation created with VERIFYING status");
  }
}
