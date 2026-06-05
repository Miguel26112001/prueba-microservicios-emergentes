package com.example.ai.agent.infrastructure.clients.profiles.requests;

public record UpdateProfileRequest(
    String name,
    String email
) {
}
