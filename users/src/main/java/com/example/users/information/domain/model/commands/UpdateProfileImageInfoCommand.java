package com.example.users.information.domain.model.commands;

public record UpdateProfileImageInfoCommand(
    Long userId,
    String imageUrl,
    String publicId
) {
}
