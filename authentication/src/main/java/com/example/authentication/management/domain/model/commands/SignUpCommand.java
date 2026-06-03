package com.example.authentication.management.domain.model.commands;

import com.example.authentication.management.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(
    String username,
    String password,
    List<Role> roles
) {
}