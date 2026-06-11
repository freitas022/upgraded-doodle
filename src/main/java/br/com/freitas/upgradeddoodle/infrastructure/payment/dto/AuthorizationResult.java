package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record AuthorizationResult(

        @NotBlank String transactionId,
        @NotNull PaymentStatus status,
        @NotBlank String authorizationCode,
        @NotNull Instant authorizedAt
) {
}
