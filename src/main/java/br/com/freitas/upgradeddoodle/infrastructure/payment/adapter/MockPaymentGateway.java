package br.com.freitas.upgradeddoodle.infrastructure.payment.adapter;

import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.AuthorizationResult;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.CaptureResult;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.PaymentCommand;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.RefundResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(name = "payment.mock.enabled", havingValue = "true")
@RequiredArgsConstructor
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public AuthorizationResult authorize(PaymentCommand command) {
        log.info("MockPaymentGateway: Autorização simulada para pedido {}", command.orderId());

        var transactionId = "MOCK-" + UUID.randomUUID();
        var authCode = "AUTH-" + System.currentTimeMillis();

        return new AuthorizationResult(
                transactionId,
                PaymentStatus.AUTHORIZED,
                authCode,
                Instant.now()
        );
    }

    @Override
    public CaptureResult capture(String transactionId) {
        log.info("MockPaymentGateway: Captura simulada para transaction {}", transactionId);

        return new CaptureResult(
                transactionId,
                PaymentStatus.CAPTURED,
                Instant.now()
        );
    }

    @Override
    public RefundResult refund(String transactionId, BigDecimal amount) {
        log.info("MockPaymentGateway: Reembolso simulado de {} para transaction {}",
                amount, transactionId);

        return new RefundResult(
                transactionId,
                PaymentStatus.REFUNDED,
                amount,
                Instant.now()
        );
    }
}
