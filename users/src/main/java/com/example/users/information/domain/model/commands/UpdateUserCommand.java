package com.example.users.information.domain.model.commands;

public record UpdateUserCommand(
    Long userId,
    String name,
    String email) {
}
