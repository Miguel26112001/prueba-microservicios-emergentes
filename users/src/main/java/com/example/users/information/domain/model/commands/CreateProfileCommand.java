package com.example.users.information.domain.model.commands;

public record CreateProfileCommand(
    String name,
    String email) {
}
