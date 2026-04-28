package com.example.notifications.emails.domain.model.events;

import java.math.BigDecimal;

public record OrderItemEvent(
    String productName,
    BigDecimal productPrice,
    Integer quantity,
    BigDecimal subtotal
) {
}
