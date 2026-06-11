package br.com.freitas.upgradeddoodle.infrastructure.payment.strategy;

import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;
import br.com.freitas.upgradeddoodle.domain.service.PaymentAuthorizationService;
import br.com.freitas.upgradeddoodle.domain.service.PaymentCaptureService;
import br.com.freitas.upgradeddoodle.infrastructure.payment.adapter.PaymentProcessor;
import br.com.freitas.upgradeddoodle.presentation.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditCardPaymentProcessor implements PaymentProcessor {

    private final PaymentAuthorizationService authorizationService;
    private final PaymentCaptureService captureService;

    @Override
    public PaymentMethod supports() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    public void process(Order order, Payment payment, PaymentRequest request) {

        log.info("Processing credit card payment {}", payment.getId());

        authorizationService.authorize(payment, request.card());
        captureService.capture(payment);
    }
}
