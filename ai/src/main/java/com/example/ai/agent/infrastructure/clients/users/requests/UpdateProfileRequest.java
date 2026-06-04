package com.example.ai.agent.infrastructure.clients.users.requests;

public record UpdateProfileRequest(
    String name,
    String email
) {
}
