package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AuthorizationCommand(
        @NotNull Long paymentId,
        @NotNull BigDecimal amount,
        @NotNull CurrencyCode currency,
        @NotNull CardData card
) {
}
