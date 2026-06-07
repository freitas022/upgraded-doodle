package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.repository.PaymentRepository;
import br.com.freitas.upgradeddoodle.infrastructure.payment.adapter.PaymentGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCaptureService {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void capture(Payment payment) {
        var result = paymentGateway.capture(payment.getTransactionId());

        payment.capture(result.capturedAt());
        paymentRepository.save(payment);

        log.info("Payment captured successfully. transactionId={}", result.transactionId());
    }
}
