package com.example.notifications.emails.domain.model.events;

public record ProfileCreatedEvent(
    String name,
    String email
) {
}