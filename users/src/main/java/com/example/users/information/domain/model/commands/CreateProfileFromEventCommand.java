package com.example.users.information.domain.model.commands;

public record CreateProfileFromEventCommand(
    Long userId,
    String name,
    String email
) {
}