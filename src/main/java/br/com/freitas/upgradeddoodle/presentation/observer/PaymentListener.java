package br.com.freitas.upgradeddoodle.presentation.observer;

import br.com.freitas.upgradeddoodle.domain.event.OrderConfirmedEvent;
import br.com.freitas.upgradeddoodle.domain.service.OrderService;
import br.com.freitas.upgradeddoodle.domain.service.PaymentService;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentListener {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOrderConfirmedEvent(OrderConfirmedEvent event) {
        log.info("Processing payment for order {}", event.orderId());

        try {
            var order = orderService.findById(event.orderId());
            var mockCardToken = "mock-token-" + UUID.randomUUID();

            paymentService.processPaymentForOrder(order, mockCardToken);

            log.info("Payment processed successfully for order {}", event.orderId());
        } catch (BusinessException ex) {
            log.warn("Business error while processing payment for order {}", event.orderId(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while processing payment for order {}", event.orderId(), ex);
            throw new RuntimeException("Payment gateway error", ex);
        }
    }
}
