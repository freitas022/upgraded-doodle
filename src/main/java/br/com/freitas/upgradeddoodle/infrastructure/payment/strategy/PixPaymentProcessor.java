package br.com.freitas.upgradeddoodle.infrastructure.payment.strategy;

import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;
import br.com.freitas.upgradeddoodle.infrastructure.payment.adapter.PaymentProcessor;
import br.com.freitas.upgradeddoodle.domain.repository.PaymentRepository;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;
import br.com.freitas.upgradeddoodle.presentation.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PixPaymentProcessor implements PaymentProcessor {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentMethod supports() {
        return PaymentMethod.PIX;
    }

    @Override
    public void process(Order order, Payment payment, PaymentRequest request) {
        var pix = request.pix();

        log.info("Processing PIX payment for order {}", order.getId());

        var transactionId = "PIX-" + UUID.randomUUID();
        payment.complete(transactionId,  Instant.now());
        paymentRepository.save(payment);
    }
}
