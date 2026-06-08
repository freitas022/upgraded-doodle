package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.event.PaymentAuthorizationFailedEvent;
import br.com.freitas.upgradeddoodle.domain.event.PaymentCaptureFailedEvent;
import br.com.freitas.upgradeddoodle.domain.event.PaymentCapturedEvent;
import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;
import br.com.freitas.upgradeddoodle.domain.repository.PaymentRepository;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentAuthorizationService authorizationService;
    private final PaymentCaptureService captureService;
    private final OrderService orderService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void processPaymentForOrder(Order order, String cardToken) {
        var payment = Payment.create(order, PaymentMethod.CREDIT_CARD);
        paymentRepository.save(payment);

        try {
            authorizationService.authorize(payment, cardToken);
            log.info("Payment authorized: {}", payment.getId());
        } catch (BusinessException e) {
            handleAuthorizationFailure(payment, e);
            throw e;
        }

        try {
            captureService.capture(payment);
            log.info("Payment captured: {}", payment.getId());
        } catch (BusinessException e) {
            handleCaptureFailure(payment, e);
            throw e;
        }

        eventPublisher.publishEvent(new PaymentCapturedEvent(payment.getId()));
        orderService.markAsPaid(order.getId());
    }

    private void handleAuthorizationFailure(Payment payment, BusinessException e) {
        payment.cancel();
        paymentRepository.save(payment);
        eventPublisher.publishEvent(new PaymentAuthorizationFailedEvent(payment, e));
        log.error("Authorization failed for payment {}: {}", payment.getId(), e.getMessage());
    }

    private void handleCaptureFailure(Payment payment, BusinessException e) {
        payment.cancel();
        paymentRepository.save(payment);
        eventPublisher.publishEvent(new PaymentCaptureFailedEvent(payment, e));
        log.error("Capture failed for payment {}: {}", payment.getId(), e.getMessage());
    }
}
