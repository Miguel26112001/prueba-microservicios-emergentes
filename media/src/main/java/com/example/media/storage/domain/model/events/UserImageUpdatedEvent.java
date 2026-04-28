package com.example.media.storage.domain.model.events;

public record UserImageUpdatedEvent(
    Long userId,
    String imageUrl,
    String publicId
) {
}