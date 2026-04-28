package com.example.users.information.domain.model.events;

public record UserImageUpdatedEvent(
    Long userId,
    String imageUrl,
    String publicId
) {
}