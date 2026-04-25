package com.example.sales.orders.application.internal.commandservices;

import com.example.sales.orders.domain.model.aggregates.Order;
import com.example.sales.orders.domain.model.commands.CreateOrderCommand;
import com.example.sales.orders.domain.model.commands.DeleteOrderCommand;
import com.example.sales.orders.domain.model.commands.OrderDetailCommand;
import com.example.sales.orders.domain.model.commands.UpdateOrderCommand;
import com.example.sales.orders.domain.model.entities.OrderDetail;
import com.example.sales.orders.domain.services.OrderCommandService;
import com.example.sales.orders.infrastructure.persistence.jpa.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

  private final OrderRepository orderRepository;

  public OrderCommandServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Optional<Order> handle(CreateOrderCommand command) {

    var order = Order.builder()
      .userId(command.userId())
      .orderDate(LocalDateTime.now())
      .total(BigDecimal.ZERO)
      .build();

    applyDetails(order, command.details());

    var savedOrder = orderRepository.save(order);

    return Optional.of(savedOrder);
  }

  @Override
  public Optional<Order> handle(UpdateOrderCommand command) {

    var orderOptional = orderRepository.findById(command.orderId());
    if (orderOptional.isEmpty()) {
      return Optional.empty();
    }

    var order = orderOptional.get();
    order.setUserId(command.userId());
    order.getDetails().clear();

    applyDetails(order, command.details());

    var updatedOrder = orderRepository.save(order);

    return Optional.of(updatedOrder);
  }

  @Override
  public void handle(DeleteOrderCommand command) {
    if (orderRepository.existsById(command.orderId())) {
      orderRepository.deleteById(command.orderId());
    }
  }

  private void applyDetails(
    Order order,
    List<OrderDetailCommand> detailCommands
  ) {

    order.getDetails().clear();

    for (OrderDetailCommand detailCommand : detailCommands) {
      var detail = OrderDetail.builder()
        .productId(detailCommand.productId())
        .quantity(detailCommand.quantity())
        .subtotal(detailCommand.subtotal())
        .build();

      order.addDetail(detail);
    }

    order.calculateTotal();
  }
}
