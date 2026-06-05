package com.example.authentication.management.interfaces.rest.resources;

import java.util.List;

public record SignUpResource(
    String username,
    String password,
    String name,
    String email,
    List<String> roles
) {
}