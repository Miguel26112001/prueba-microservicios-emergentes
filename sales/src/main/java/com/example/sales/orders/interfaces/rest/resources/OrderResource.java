package com.example.sales.orders.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResource(
  Long id,
  Long userId,
  LocalDateTime orderDate,
  BigDecimal total,
  List<OrderDetailResource> details
) {
}
