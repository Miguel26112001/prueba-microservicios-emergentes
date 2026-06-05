package com.example.ai.agent.infrastructure.clients.profiles.requests;

public record CreateProfileRequest(
    String name,
    String email
) {
}
