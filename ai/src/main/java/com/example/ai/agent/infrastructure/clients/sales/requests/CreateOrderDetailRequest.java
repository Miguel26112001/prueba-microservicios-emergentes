package com.example.ai.agent.infrastructure.clients.sales.requests;

public record CreateOrderDetailRequest(
    Long productId,
    Integer quantity
) {
}