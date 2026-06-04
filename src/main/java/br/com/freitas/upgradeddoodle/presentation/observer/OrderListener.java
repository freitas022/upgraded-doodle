package br.com.freitas.upgradeddoodle.presentation.observer;

import br.com.freitas.upgradeddoodle.domain.event.OrderCancelledEvent;
import br.com.freitas.upgradeddoodle.domain.event.OrderConfirmedEvent;
import br.com.freitas.upgradeddoodle.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderListener {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderConfirmedEvent(OrderConfirmedEvent event) {
        emailService.sendOrderConfirmationEmail(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCancelledEvent(OrderCancelledEvent event) {
        emailService.sendOrderCancelledEmail(event);
    }
}
