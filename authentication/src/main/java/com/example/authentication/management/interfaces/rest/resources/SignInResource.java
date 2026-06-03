package com.example.authentication.management.interfaces.rest.resources;

public record SignInResource(
    String username,
    String password
) {
}