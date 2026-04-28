package com.example.media.storage.domain.model.responses;

public record ImageUploadResponse(
    String imageUrl,
    String publicId
) {
}
