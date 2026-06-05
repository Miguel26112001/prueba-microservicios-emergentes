package com.example.ai.agent.domain.model.responses;

import java.math.BigDecimal;

public record ProductResource(
    Long id,
    String name,
    BigDecimal price,
    Integer stock
) {
}
