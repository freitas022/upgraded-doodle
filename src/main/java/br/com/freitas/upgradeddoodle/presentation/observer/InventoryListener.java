package br.com.freitas.upgradeddoodle.presentation.observer;

import br.com.freitas.upgradeddoodle.domain.event.StockUpdatedEvent;
import br.com.freitas.upgradeddoodle.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InventoryListener {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStockBelowReorderLevel(StockUpdatedEvent event) {
        emailService.sendLowStockAlert(event.productId(), event.quantityAvailable());
    }
}
