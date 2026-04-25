package com.example.sales.products.domain.model.commands;

import java.math.BigDecimal;

public record UpdateProductCommand(
    Long productId,
    String name,
    BigDecimal price,
    Integer stock
) {
}
