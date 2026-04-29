package com.example.ai.agent.infrastructure.clients.users.requests;

public record CreateUserRequest(
    String name,
    String email
) {
}
