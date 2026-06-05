package com.example.media.storage.domain.model.events;

public record ProfileImageUpdatedEvent(
    Long profileId,
    String imageUrl,
    String publicId
) {
}