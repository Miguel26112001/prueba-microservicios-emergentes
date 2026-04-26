package com.example.sales.orders.domain.model.commands;

/**
 * Command to delete an order
 */
public record DeleteOrderCommand(
  Long orderId
) {
}
