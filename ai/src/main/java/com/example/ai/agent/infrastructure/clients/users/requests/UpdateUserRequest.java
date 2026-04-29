package com.example.ai.agent.infrastructure.clients.users.requests;

public record UpdateUserRequest(
    String name,
    String email
) {
}
