package com.example.users.information.domain.model.commands;

public record CreateUserCommand(
    String name,
    String email) {
}
