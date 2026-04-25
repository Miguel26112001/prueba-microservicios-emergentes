package com.example.sales.orders.domain.services;

import com.example.sales.orders.domain.model.aggregates.Order;
import com.example.sales.orders.domain.model.commands.CreateOrderCommand;
import com.example.sales.orders.domain.model.commands.DeleteOrderCommand;
import com.example.sales.orders.domain.model.commands.UpdateOrderCommand;

import java.util.Optional;

public interface OrderCommandService {

  Optional<Order> handle(CreateOrderCommand command);

  Optional<Order> handle(UpdateOrderCommand command);

  void handle(DeleteOrderCommand command);
}
