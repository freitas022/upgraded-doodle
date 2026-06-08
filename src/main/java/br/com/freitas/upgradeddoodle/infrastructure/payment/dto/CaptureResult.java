package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;

import java.time.Instant;

public record CaptureResult(

        String transactionId,
        PaymentStatus status,
        Instant capturedAt
) {
}
