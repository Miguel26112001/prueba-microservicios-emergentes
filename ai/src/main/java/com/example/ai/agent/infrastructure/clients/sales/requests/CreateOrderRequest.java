package com.example.ai.agent.infrastructure.clients.sales.requests;

import java.util.List;

public record CreateOrderRequest(
    Long profileId,
    List<CreateOrderDetailRequest> details
) {
}