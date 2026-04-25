package com.example.sales.orders.interfaces.rest.resources;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderDetailResource(
  @NotNull(message = "Product id is required")
  Long productId,

  @NotNull(message = "Quantity is required")
  @Min(value = 1, message = "Quantity must be at least 1")
  Integer quantity
) {
}
