package com.example.authentication.management.domain.model.events;

public record UserCreatedEvent(
    Long userId,
    String name,
    String email
) {
}