package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.event.OrderCancelledEvent;
import br.com.freitas.upgradeddoodle.domain.event.OrderConfirmedEvent;
import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.OrderItem;
import br.com.freitas.upgradeddoodle.domain.repository.OrderRepository;
import br.com.freitas.upgradeddoodle.presentation.dto.CreateOrderItemRequest;
import br.com.freitas.upgradeddoodle.presentation.dto.CreateOrderRequest;
import br.com.freitas.upgradeddoodle.presentation.dto.OrderCreatedResponse;
import br.com.freitas.upgradeddoodle.presentation.dto.OrderDetailResponse;
import br.com.freitas.upgradeddoodle.presentation.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final InventoryService inventoryService;
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

    public OrderDetailResponse findOrderDetailsById(Long id) {
        Order order = orderRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id
                ));

        return OrderDetailResponse.fromEntity(order);
    }

    @Transactional
    public OrderCreatedResponse create(CreateOrderRequest request) {
        var customer = customerService.findById(request.customerId());

        var order = Order.create(customer);

        var productIds = request.items().stream()
                .map(CreateOrderItemRequest::productId)
                .distinct()
                .toList();

        var productsById = productService.findAvailableProductsByIds(productIds);

        request.items().forEach(itemRequest -> {
            var product = productsById.get(itemRequest.productId());

            order.addItem(OrderItem.create(
                    product,
                    itemRequest.quantity(),
                    product.getPrice()
            ));
        });

        return OrderCreatedResponse.fromEntity(orderRepository.save(order));
    }


    @Transactional
    public OrderDetailResponse confirm(Long orderId) {
        var order = findById(orderId);
        order.confirm();

        decreaseItemsFromStock(order);

        eventPublisher.publishEvent(new OrderConfirmedEvent(
                order.getId(), order.getCustomer().getEmail()
        ));

        return OrderDetailResponse.fromEntity(orderRepository.save(order));
    }

    @Transactional
    public OrderDetailResponse cancel(Long orderId) {
        var order = findById(orderId);

        boolean shouldReturnStock = order.isConfirmed();

        order.cancel();

        if (shouldReturnStock) {
            returnItemsToStock(order);
        }

        orderRepository.save(order);

        eventPublisher.publishEvent(new OrderCancelledEvent(order.getId(), order.getCustomer().getEmail()));
        return OrderDetailResponse.fromEntity(order);
    }

    private void decreaseItemsFromStock(Order order) {
        for (OrderItem item : order.getItems()) {
            inventoryService.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }
    }

    private void returnItemsToStock(Order order) {
        for (OrderItem item : order.getItems()) {
           inventoryService.increaseStock(item.getProduct().getId(), item.getQuantity());
        }
    }
}
