package com.example.sales.orders.interfaces.rest.resources;

import java.math.BigDecimal;

public record OrderDetailResource(
  Long id,
  Long productId,
  Integer quantity,
  BigDecimal subtotal
) {
}
