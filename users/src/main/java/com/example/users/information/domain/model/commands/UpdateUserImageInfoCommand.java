package com.example.users.information.domain.model.commands;

public record UpdateUserImageInfoCommand(
    Long userId,
    String imageUrl,
    String publicId
) {
}
