package com.example.ai.agent.infrastructure.clients.sales.requests;

import java.math.BigDecimal;

public record CreateProductRequest(
    String name,
    BigDecimal price,
    Integer stock
) {
}