package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import java.math.BigDecimal;

public record RefundCommand(
        String transactionId,
        BigDecimal amount,
        String currency,
        String reason
) {}
