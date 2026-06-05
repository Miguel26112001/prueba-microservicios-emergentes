package com.example.ai.agent.domain.model.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResource(
    Long id,
    Long profileId,
    LocalDateTime orderDate,
    BigDecimal total
) {
}