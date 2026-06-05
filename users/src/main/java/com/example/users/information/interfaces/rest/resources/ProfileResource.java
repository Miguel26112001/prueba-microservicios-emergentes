package com.example.users.information.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Profile resource representation", title = "Profile")
public record ProfileResource(

    @Schema(description = "Unique identifier of the profile",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY,
        requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "Full name of the profile",
        example = "John Doe",
        minLength = 1,
        maxLength = 100,
        requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(description = "Email address of the profile (must be unique)",
        example = "john.doe@example.com",
        pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
        requiredMode = Schema.RequiredMode.REQUIRED)
    String email
) {
}
