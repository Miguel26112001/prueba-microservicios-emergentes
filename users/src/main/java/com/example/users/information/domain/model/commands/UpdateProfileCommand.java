package com.example.users.information.domain.model.commands;

public record UpdateProfileCommand(
    Long userId,
    String name,
    String email) {
}
