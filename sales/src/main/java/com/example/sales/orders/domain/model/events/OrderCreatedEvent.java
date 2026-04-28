package com.example.sales.orders.domain.model.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedEvent(
    String name,
    String email,
    LocalDateTime orderDate,
    BigDecimal total,
    List<OrderItemEvent> items
) {
}
