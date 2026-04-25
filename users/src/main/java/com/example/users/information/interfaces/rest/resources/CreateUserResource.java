package com.example.users.information.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Resource for creating a new user")
public record CreateUserResource(

    @Schema(
        description = "Full name of the user",
        example = "John Doe",
        minLength = 2,
        maxLength = 100,
        requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúñÑ\\s]+$", message = "Name can only contain letters and spaces")
    String name,

    @Schema(
        description = "Email address of the user (must be unique)",
        example = "john.doe@example.com",
        pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
        maxLength = 120,
        requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    @Size(max = 120, message = "Email must not exceed 120 characters")
    String email
) {
}