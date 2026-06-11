package br.com.freitas.upgradeddoodle.infrastructure.payment.adapter;

import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@Validated
@ConditionalOnProperty(name = "payment.mock.enabled", havingValue = "true")
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public AuthorizationResult authorize(AuthorizationCommand command) {
        log.info("Mock authorizing paymentId={} amount={} currency={}",
                command.paymentId(),
                command.amount(),
                command.currency());

        return new AuthorizationResult(
                "TXN-" + UUID.randomUUID(),
                PaymentStatus.AUTHORIZED,
                "AUTH-" + ThreadLocalRandom.current().nextInt(100000, 999999),
                Instant.now()
        );
    }

    @Override
    public CaptureResult capture(CaptureCommand command) {
        log.info("Mock capturing transactionId={} amount={} currency={}",
                command.transactionId(),
                command.amount(),
                command.currency());

        return new CaptureResult(
                command.transactionId(),
                PaymentStatus.CAPTURED,
                Instant.now()
        );
    }

    @Override
    public RefundResult refund(RefundCommand command) {
        log.info("Mock refunding transactionId={} amount={} currency={}",
                command.transactionId(),
                command.amount(),
                command.currency());

        return new RefundResult(
                command.transactionId(),
                PaymentStatus.REFUNDED,
                command.amount(),
                Instant.now()
        );
    }
}
