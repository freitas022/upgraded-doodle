package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;
import br.com.freitas.upgradeddoodle.domain.repository.PaymentRepository;
import br.com.freitas.upgradeddoodle.infrastructure.payment.adapter.PaymentGateway;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.CaptureCommand;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.CaptureResult;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.CurrencyCode;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class PaymentCaptureService {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    public void capture(@NotNull Payment payment) {
        log.info("Capturing payment {}", payment.getId());

        var command = new CaptureCommand(
                payment.getTransactionId(),
                payment.getAmount(),
                CurrencyCode.BRL
        );

        var result = paymentGateway.capture(command);

        ensureCaptured(result);

        payment.capture(result.capturedAt());
        paymentRepository.save(payment);

        log.info("Payment captured successfully. transactionId={}", result.transactionId());
    }

    private void ensureCaptured(CaptureResult result) {
        if (!PaymentStatus.CAPTURED.equals(result.status())) {
            throw new BusinessException("Payment capture failed");
        }
    }
}
