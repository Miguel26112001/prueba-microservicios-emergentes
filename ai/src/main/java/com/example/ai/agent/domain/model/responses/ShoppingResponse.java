package com.example.ai.agent.domain.model.responses;

import java.util.List;

public record ShoppingResponse(
    OrderResource order,
    List<ProductMatchResponse> ambiguousProducts,
    List<String> notFoundProducts
) {
}