package br.com.freitas.upgradeddoodle.presentation.observer;

import br.com.freitas.upgradeddoodle.domain.event.PaymentAuthorizationFailedEvent;
import br.com.freitas.upgradeddoodle.domain.event.PaymentCaptureFailedEvent;
import br.com.freitas.upgradeddoodle.domain.event.PaymentCapturedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class PaymentListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentCapturedEvent(PaymentCapturedEvent event) {
        log.info(
                "Payment captured successfully. paymentId={}, orderId={}",
                event.paymentId(),
                event.orderId()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentAuthorizationFailedEvent(PaymentAuthorizationFailedEvent event) {
        log.warn(
                "Payment authorization failed. paymentId={}, orderId={}, reason={}",
                event.paymentId(),
                event.orderId(),
                event.reason()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentCaptureFailedEvent(PaymentCaptureFailedEvent event) {
        log.warn(
                "Payment capture failed. paymentId={}, orderId={}, reason={}",
                event.paymentId(),
                event.orderId(),
                event.reason()
        );
    }
}
