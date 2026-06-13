package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.event.StockUpdatedEvent;
import br.com.freitas.upgradeddoodle.domain.model.Inventory;
import br.com.freitas.upgradeddoodle.domain.repository.InventoryRepository;
import br.com.freitas.upgradeddoodle.presentation.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Inventory findByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found for product id: " + productId
                        )
                );
    }

    @Transactional
    public void increaseStock(Long productId, Integer qty) {
        var inventory = findByProductId(productId);
        inventory.increase(qty);
    }

    @Transactional
    public void decreaseStock(Long productId, Integer qty) {
        var inventory = findByProductId(productId);
        inventory.decrease(qty);

        if (inventory.isBelowReorderLevel()) {
            eventPublisher.publishEvent(
                    new StockUpdatedEvent(productId, inventory.getQuantityAvailable())
            );
        }
    }
}
