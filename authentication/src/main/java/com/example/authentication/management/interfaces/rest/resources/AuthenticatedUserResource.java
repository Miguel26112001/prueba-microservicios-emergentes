package com.example.authentication.management.interfaces.rest.resources;

public record AuthenticatedUserResource(
    Long id,
    String username,
    String token
) {
}