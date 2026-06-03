package com.example.sales.orders.domain.model.commands;

import java.util.List;

/**
 * Command to create a new order
 */
public record CreateOrderCommand(
  Long profileId,
  List<OrderDetailCommand> details
) {
}
