package com.example.authentication.management.domain.model.commands;

public record SignInCommand(
    String username,
    String password
) {
}