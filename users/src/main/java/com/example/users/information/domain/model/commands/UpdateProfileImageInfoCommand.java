package com.example.users.information.domain.model.commands;

public record UpdateProfileImageInfoCommand(
    Long profileId,
    String imageUrl,
    String publicId
) {
}
