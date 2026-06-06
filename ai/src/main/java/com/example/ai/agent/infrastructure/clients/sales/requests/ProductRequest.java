package com.example.ai.agent.infrastructure.clients.sales.requests;

public record ProductRequest(
    String searchTerm,
    int quantity
) {
}