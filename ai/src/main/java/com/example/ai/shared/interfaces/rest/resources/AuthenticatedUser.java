package com.example.ai.shared.interfaces.rest.resources;

import java.util.List;

public record AuthenticatedUser(
    Long userId,
    String username,
    List<String> roles
) {
}