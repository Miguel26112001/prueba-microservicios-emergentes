package com.example.sales.orders.domain.model.commands;

/**
 * Command used inside CreateOrderCommand / UpdateOrderCommand
 */
public record OrderDetailCommand(
  Long productId,
  Integer quantity) {
}
