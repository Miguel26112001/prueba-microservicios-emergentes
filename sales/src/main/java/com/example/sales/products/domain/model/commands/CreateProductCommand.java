package com.example.sales.products.domain.model.commands;

import java.math.BigDecimal;

public record CreateProductCommand(
    String name,
    BigDecimal price,
    Integer stock
) {
}
