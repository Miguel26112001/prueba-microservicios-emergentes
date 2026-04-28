package com.example.sales.orders.domain.model.valueobjects;

import java.math.BigDecimal;

public record ProductData(
    String name,
    BigDecimal price
) {
}
