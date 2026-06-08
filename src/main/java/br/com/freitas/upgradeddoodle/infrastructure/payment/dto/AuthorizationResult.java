package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;

import java.time.Instant;

public record AuthorizationResult(

        String transactionId,
        PaymentStatus status,
        String authorizationCode,
        Instant authorizedAt
) {
}
