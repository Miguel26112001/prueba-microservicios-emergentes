package com.example.sales.orders.domain.model.commands;

import java.math.BigDecimal;

/**
 * Command used inside CreateOrderCommand / UpdateOrderCommand
 */
public record OrderDetailCommand(
  Long productId,
  Integer quantity) {
}
