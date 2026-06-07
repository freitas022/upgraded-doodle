package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.event.OrderCancelledEvent;
import br.com.freitas.upgradeddoodle.domain.event.OrderConfirmedEvent;
import br.com.freitas.upgradeddoodle.domain.model.Inventory;
import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.OrderItem;
import br.com.freitas.upgradeddoodle.domain.model.enums.OrderStatus;
import br.com.freitas.upgradeddoodle.domain.repository.CustomerRepository;
import br.com.freitas.upgradeddoodle.domain.repository.InventoryRepository;
import br.com.freitas.upgradeddoodle.domain.repository.OrderRepository;
import br.com.freitas.upgradeddoodle.domain.repository.ProductRepository;
import br.com.freitas.upgradeddoodle.presentation.dto.CreateOrderRequest;
import br.com.freitas.upgradeddoodle.presentation.dto.OrderCreatedResponse;
import br.com.freitas.upgradeddoodle.presentation.dto.OrderDetailResponse;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import br.com.freitas.upgradeddoodle.presentation.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    public void markAsPaid(Long orderId) {
        var order = findById(orderId);
        order.markAsPaid();
        orderRepository.save(order);
    }

    @Transactional
    public OrderDetailResponse findOrderDetailsById(Long id) {
        Order order = orderRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id
                ));

        return OrderDetailResponse.fromEntity(order);
    }

    @Transactional
    public OrderCreatedResponse create(CreateOrderRequest request) {

        var customer = customerRepository.getReferenceById(request.customerId());

        var order = Order.create(customer);

        var items = request.items().stream()
                .map(item -> {
                            var product = productRepository.findById(item.productId())
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            "Product not found with id: " + item.productId()
                                    ));

                            if (!product.isAvailable()) {
                                throw new BusinessException("Product with id " + product.getId() + " is not available.");
                            }

                            return OrderItem.create(
                                    product,
                                    item.quantity(),
                                    product.getPrice()
                            );
                        }
                )
                .toList();

        items.forEach(order::addItem);
        return OrderCreatedResponse.fromEntity(orderRepository.save(order));
    }


    @Transactional
    public OrderDetailResponse confirm(Long orderId) {
        var order = findById(orderId);
        order.confirm();

        for (OrderItem item : order.getItems()) {
            var inventory = inventoryRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product id: " + item.getProduct().getId()
                    ));

            if (!inventory.hasStock(item.getQuantity())) {
                throw new BusinessException(
                        "Insufficient stock for product id: " + item.getProduct().getId()
                );
            }

            inventory.decrease(item.getQuantity());
        }

        eventPublisher.publishEvent(new OrderConfirmedEvent(
                order.getId(), order.getCustomer().getEmail()
        ));

        return OrderDetailResponse.fromEntity(orderRepository.save(order));
    }

    @Transactional
    public OrderDetailResponse cancel(Long orderId) {
        var order = findById(orderId);

        boolean shouldReturnStock = order.getStatus() == OrderStatus.CONFIRMED;

        order.cancel();

        if (shouldReturnStock) {
            for (OrderItem item : order.getItems()) {
                Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Inventory not found for product id: " + item.getProduct().getId()
                        ));

                inventory.increase(item.getQuantity());
            }
        }
        eventPublisher.publishEvent(new OrderCancelledEvent(order.getId(), order.getCustomer().getEmail()));
        return OrderDetailResponse.fromEntity(order);
    }
}
