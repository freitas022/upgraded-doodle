package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record RefundResult(

        String transactionId,
        PaymentStatus status,
        BigDecimal refundedAmount,
        Instant refundedAt

) {
}
