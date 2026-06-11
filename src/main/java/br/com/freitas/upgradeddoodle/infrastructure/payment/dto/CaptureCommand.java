package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CaptureCommand(
        @NotBlank String transactionId,
        @NotNull BigDecimal amount,
        @NotNull CurrencyCode currency
) {
}
