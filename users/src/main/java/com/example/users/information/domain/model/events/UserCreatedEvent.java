package com.example.users.information.domain.model.events;

public record UserCreatedEvent(
    String name,
    String email
) {
}
