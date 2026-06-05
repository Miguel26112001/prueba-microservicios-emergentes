package com.example.users.information.domain.model.events;

public record ProfileImageUpdatedEvent(
    Long profileId,
    String imageUrl,
    String publicId
) {
}