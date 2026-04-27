package com.example.notifications.emails.domain.model.events;

public record UserCreatedEvent(
    String name,
    String email
) {
}
