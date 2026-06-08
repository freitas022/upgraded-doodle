package br.com.freitas.upgradeddoodle.infrastructure.payment.adapter;

import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.AuthorizationResult;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.CaptureResult;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.PaymentCommand;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.RefundResult;

import java.math.BigDecimal;


public interface PaymentGateway {

    AuthorizationResult authorize(PaymentCommand command);

    CaptureResult capture(String transactionId);

    RefundResult refund(String transactionId, BigDecimal amount);
}
