package com.example.users.information.domain.model.events;

public record UserCreatedEvent(
    Long userId,
    String name,
    String email
) {
}