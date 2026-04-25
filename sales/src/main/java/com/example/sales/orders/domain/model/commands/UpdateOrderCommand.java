package com.example.sales.orders.domain.model.commands;

import java.util.List;

/**
 * Command to update an existing order
 */
public record UpdateOrderCommand(
  Long orderId,
  Long userId,
  List<OrderDetailCommand> details
) {
}
