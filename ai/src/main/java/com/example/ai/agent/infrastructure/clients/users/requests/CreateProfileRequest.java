package com.example.ai.agent.infrastructure.clients.users.requests;

public record CreateProfileRequest(
    String name,
    String email
) {
}
