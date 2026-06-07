package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;
import br.com.freitas.upgradeddoodle.domain.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentAuthorizationService paymentAuthorizationService;
    private final PaymentCaptureService paymentCaptureService;

    @Transactional
    public void processPaymentForOrder(Order order, String cardToken) {
        var existingPayment = paymentRepository.findByOrderId(order.getId());
        if (existingPayment.isPresent()) {
            log.warn("Payment already exists for order {}", order.getId());
            return;
        }

        var payment = Payment.create(order, PaymentMethod.CREDIT_CARD);
        paymentRepository.save(payment);

        paymentAuthorizationService.authorize(payment, cardToken);
        paymentCaptureService.capture(payment);

        order.markAsPaid();

        log.info("Order {} marked as PAID", order.getId());
    }
}
