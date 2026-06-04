package com.example.media.storage.domain.model.events;

public record ProfileImageUpdatedEvent(
    Long userId,
    String imageUrl,
    String publicId
) {
}