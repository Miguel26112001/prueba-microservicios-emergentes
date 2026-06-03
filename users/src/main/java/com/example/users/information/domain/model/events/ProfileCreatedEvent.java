package com.example.users.information.domain.model.events;

public record ProfileCreatedEvent(
    String name,
    String email
) {
}
