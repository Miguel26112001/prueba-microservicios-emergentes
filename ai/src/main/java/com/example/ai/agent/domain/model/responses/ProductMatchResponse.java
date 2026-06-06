package com.example.ai.agent.domain.model.responses;

import java.util.List;

public record ProductMatchResponse(
    String searchTerm,
    Integer quantity,
    List<ProductResource> matches
) {
}