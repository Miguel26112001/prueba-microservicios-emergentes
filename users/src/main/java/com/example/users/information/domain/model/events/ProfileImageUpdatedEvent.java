package com.example.users.information.domain.model.events;

public record ProfileImageUpdatedEvent(
    Long userId,
    String imageUrl,
    String publicId
) {
}