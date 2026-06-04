package com.example.users.information.domain.model.commands;

public record CreateProfileCommand(
    Long userId,
    String name,
    String email
) {
}