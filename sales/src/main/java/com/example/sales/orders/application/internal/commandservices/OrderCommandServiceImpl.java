package com.example.sales.orders.application.internal.commandservices;

import com.example.sales.orders.application.integration.messaging.OrderEventPublisher;
import com.example.sales.orders.domain.model.aggregates.Order;
import com.example.sales.orders.domain.model.commands.CreateOrderCommand;
import com.example.sales.orders.domain.model.commands.DeleteOrderCommand;
import com.example.sales.orders.domain.model.commands.OrderDetailCommand;
import com.example.sales.orders.domain.model.commands.UpdateOrderCommand;
import com.example.sales.orders.domain.model.entities.OrderDetail;
import com.example.sales.orders.domain.model.events.OrderCreatedEvent;
import com.example.sales.orders.domain.model.events.OrderItemEvent;
import com.example.sales.orders.domain.model.valueobjects.ProductData;
import com.example.sales.orders.domain.services.OrderCommandService;
import com.example.sales.orders.domain.services.ProductExternalService;
import com.example.sales.orders.domain.services.UserExternalService;
import com.example.sales.orders.infrastructure.persistence.jpa.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

  private final OrderRepository orderRepository;
  private final ProductExternalService productExternalService;
  private final UserExternalService userExternalService;
  private final OrderEventPublisher orderEventPublisher;

  public OrderCommandServiceImpl(
      OrderRepository orderRepository,
      ProductExternalService productExternalService,
      UserExternalService userExternalService, OrderEventPublisher orderEventPublisher) {
    this.orderRepository = orderRepository;
    this.productExternalService = productExternalService;
    this.userExternalService = userExternalService;
    this.orderEventPublisher = orderEventPublisher;
  }

  @Override
  public Optional<Order> handle(CreateOrderCommand command) {

    var userOptional =
        userExternalService.getUserById(command.userId());

    if (userOptional.isEmpty()) {
      throw new RuntimeException("User not found");
    }

    var order = Order.builder()
        .userId(command.userId())
        .orderDate(LocalDateTime.now())
        .total(BigDecimal.ZERO)
        .build();

    List<OrderItemEvent> items =
        applyDetails(order, command.details());

    var savedOrder = orderRepository.save(order);

    var user = userOptional.get();

    orderEventPublisher.publishOrderCreated(
        new OrderCreatedEvent(
            user.name(),
            user.email(),
            savedOrder.getOrderDate(),
            savedOrder.getTotal(),
            items
        )
    );

    return Optional.of(savedOrder);
  }

  @Override
  public Optional<Order> handle(UpdateOrderCommand command) {

    var orderOptional =
        orderRepository.findById(command.orderId());

    if (orderOptional.isEmpty()) {
      return Optional.empty();
    }

    var order = orderOptional.get();

    restoreStock(order);

    applyDetails(order, command.details());

    var updatedOrder =
        orderRepository.save(order);

    return Optional.of(updatedOrder);
  }

  @Override
  public void handle(DeleteOrderCommand command) {

    var order = orderRepository
        .findById(command.orderId())
        .orElse(null);

    if (order == null) {
      return;
    }

    restoreStock(order);

    orderRepository.delete(order);
  }

  private List<OrderItemEvent> applyDetails(
      Order order,
      List<OrderDetailCommand> detailCommands
  ) {

    order.getDetails().clear();

    List<OrderItemEvent> items = new ArrayList<>();

    for (OrderDetailCommand detailCommand : detailCommands) {

      Long productId = detailCommand.productId();

      Integer quantity = detailCommand.quantity();

      validateProduct(productId, quantity);

      var product = productExternalService.getProductData(productId);

      BigDecimal subtotal = product.price().multiply(BigDecimal.valueOf(quantity));

      var detail = OrderDetail.builder()
          .productId(productId)
          .quantity(quantity)
          .subtotal(subtotal)
          .build();

      order.addDetail(detail);

      productExternalService.reduceStock(productId, quantity);

      items.add(
          new OrderItemEvent(
              product.name(),
              product.price(),
              quantity,
              subtotal
          )
      );
    }

    order.calculateTotal();

    return items;
  }

  private void restoreStock(Order order) {

    for (var detail : order.getDetails()) {

      productExternalService.increaseStock(
          detail.getProductId(),
          detail.getQuantity()
      );
    }

    order.getDetails().clear();
  }

  private void validateProduct(
      Long productId,
      Integer quantity
  ) {

    if (!productExternalService.existsProduct(productId)) {
      throw new RuntimeException(
          "Product with id " + productId + " does not exist"
      );
    }

    if (!productExternalService.hasStock(productId, quantity)) {
      throw new RuntimeException(
          "Insufficient stock for product id " + productId
      );
    }
  }
}
