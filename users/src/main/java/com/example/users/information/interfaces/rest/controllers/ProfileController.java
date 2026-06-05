package com.example.users.information.interfaces.rest.controllers;

import com.example.users.information.domain.model.queries.GetProfileByNameQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.users.information.domain.model.commands.DeleteProfileCommand;
import com.example.users.information.domain.model.queries.GetAllProfileQuery;
import com.example.users.information.domain.model.queries.GetProfileByEmailQuery;
import com.example.users.information.domain.model.queries.GetProfileByIdQuery;
import com.example.users.information.domain.services.ProfileCommandService;
import com.example.users.information.domain.services.ProfileQueryService;
import com.example.users.information.interfaces.rest.resources.CreateProfileResource;
import com.example.users.information.interfaces.rest.resources.UpdateProfileResource;
import com.example.users.information.interfaces.rest.resources.ProfileResource;
import com.example.users.information.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import com.example.users.information.interfaces.rest.transform.UpdateProfileCommandFromResourceAssembler;
import com.example.users.information.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.example.users.shared.interfaces.rest.resources.ErrorMessageResource;

@RestController
@RequestMapping(
    value = "/api/v1/profiles",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(
    name = "Profiles",
    description = "API for managing profile resources. " +
        "Provides endpoints to retrieve profile information."
)
public class ProfileController {

  private final ProfileQueryService profileQueryService;
  private final ProfileCommandService profileCommandService;

  public ProfileController(
      ProfileQueryService profileQueryService,
      ProfileCommandService profileCommandService
  ) {

    this.profileQueryService = profileQueryService;
    this.profileCommandService = profileCommandService;
  }

  @GetMapping
  @Operation(
      summary = "Get all profiles",
      description = "Retrieves a list of all registered profiles in the system." +
          " Returns an empty array if no profiles exist.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved users list",
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ProfileResource.class)),
                  examples = @ExampleObject(
                      name = "Successful response",
                      value = """
                          [
                            {
                              "id": 1,
                              "name": "John Doe",
                              "email": "john.doe@example.com"
                            },
                            {
                              "id": 2,
                              "name": "Jane Smith",
                              "email": "jane.smith@example.com"
                            }
                          ]
                          """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "204",
              description = "No profiles found",
              content = @Content(
                  mediaType = "application/json",
                  examples = @ExampleObject(value = "[]")
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  examples = @ExampleObject(
                      value = """
                          {
                            "timestamp": "2024-01-01T00:00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "code": "INTERNAL_ERROR",
                            "message": "An unexpected error occurred",
                            "path": "/api/v1/profiles"
                          }
                          """
                  )
              )
          )
      }
  )
  public ResponseEntity<List<ProfileResource>> getAllProfiles() {

    var profiles = profileQueryService.handle(new GetAllProfileQuery());

    var profilesResources = profiles.stream()
        .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
        .toList();

    if (profilesResources.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(profilesResources);
  }

  @PostMapping
  @Operation(
      summary = "Create a new profile",
      description = "Creates a new profile with the provided information. The email must be unique and will be validated.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Profile creation data",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CreateProfileResource.class),
              examples = {
                  @ExampleObject(
                      name = "Valid profile example",
                      summary = "Valid profile data",
                      value = """
                        {
                          "name": "John Doe",
                          "email": "john.doe@example.com"
                        }
                        """
                  ),
                  @ExampleObject(
                      name = "Invalid email example",
                      summary = "Invalid email format",
                      value = """
                        {
                          "name": "Jane Smith",
                          "email": "invalid-email"
                        }
                        """
                  ),
                  @ExampleObject(
                      name = "Missing name example",
                      summary = "Missing required field",
                      value = """
                        {
                          "email": "jane.smith@example.com"
                        }
                        """
                  )
              }
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Profile created successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProfileResource.class),
                  examples = @ExampleObject(
                      name = "Created profile response",
                      value = """
                        {
                          "id": 1,
                          "name": "John Doe",
                          "email": "john.doe@example.com"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid input data or validation failed",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = {
                      @ExampleObject(
                          name = "Validation errors",
                          summary = "Bean validation failed",
                          value = """
                            {
                              "timestamp": "2024-01-01T00:00:00",
                              "status": 400,
                              "error": "Validation Failed",
                              "code": "VALIDATION_ERROR",
                              "message": "Name is required, Email should be valid",
                              "path": "/api/v1/profiles"
                            }
                            """
                      ),
                      @ExampleObject(
                          name = "Empty result",
                          summary = "Command service returned empty",
                          value = """
                            {
                              "timestamp": "2024-01-01T00:00:00",
                              "status": 400,
                              "error": "Bad Request",
                              "code": "CREATION_FAILED",
                              "message": "User could not be created",
                              "path": "/api/v1/profiles"
                            }
                            """
                      )
                  }
              )
          ),
          @ApiResponse(
              responseCode = "409",
              description = "Conflict - Email already exists",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "Duplicate email",
                      value = """
                        {
                          "timestamp": "2024-01-01T00:00:00",
                          "status": 409,
                          "error": "Conflict",
                          "code": "DUPLICATE_EMAIL",
                          "message": "Email 'john.doe@example.com' is already registered",
                          "path": "/api/v1/profiles"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2024-01-01T00:00:00",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/profiles"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<ProfileResource> createProfile(
      @RequestBody @Valid CreateProfileResource resource
  ) {
    var createProfileCommand = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);

    var profileOptional = profileCommandService.handle(createProfileCommand);
    if (profileOptional.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(null);
    }

    var profileResource = ProfileResourceFromEntityAssembler
        .toResourceFromEntity(profileOptional.get());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(profileResource);
  }

  @PutMapping("/{profileId}")
  @Operation(
      summary = "Update an existing profile",
      description = "Updates a profile's information by their unique identifier." +
          " All fields are required for full update.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Updated profile data",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UpdateProfileResource.class),
              examples = {
                  @ExampleObject(
                      name = "Valid update example",
                      summary = "Complete profile data update",
                      value = """
                        {
                          "name": "John Updated Doe",
                          "email": "john.updated@example.com"
                        }
                        """
                  )
              }
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Profile updated successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProfileResource.class),
                  examples = @ExampleObject(
                      name = "Updated profile response",
                      value = """
                        {
                          "id": 1,
                          "name": "John Updated Doe",
                          "email": "john.updated@example.com"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid input data",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      value = """
                            {
                              "timestamp": "2026-04-23T00:17:20.539",
                              "status": 400,
                              "error": "Validation Failed",
                              "code": "VALIDATION_ERROR",
                              "message": "Name is required, Email should be valid",
                              "path": "/api/v1/profiles/1"
                            }
                            """
                      )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Profile not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      value = """
                            {
                              "timestamp": "2026-04-23T00:17:20.539",
                              "status": 404,
                              "error": "Not Found",
                              "code": "PROFILE_NOT_FOUND",
                              "message": "Profile with id 1 does not exist",
                              "path": "/api/v1/profiles/1"
                            }
                            """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "409",
              description = "Conflict - Email already exists",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "Duplicate email",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 409,
                          "error": "Conflict",
                          "code": "DUPLICATE_EMAIL",
                          "message": "Email 'john.updated@example.com' is already registered by another profile",
                          "path": "/api/v1/profiles/1"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/profiles/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<ProfileResource> updateProfile(
      @Parameter(description = "Profile ID to update", example = "1", required = true)
      @PathVariable Long profileId,
      @RequestBody @Valid UpdateProfileResource resource
  ) {

    var updateProfileCommand = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(profileId, resource);

    var profileOptional = profileCommandService.handle(updateProfileCommand);
    if (profileOptional.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(null);
    }

    var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profileOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(profileResource);
  }

  @DeleteMapping("/{profileId}")
  @Operation(
      summary = "Delete a profile",
      description = "Deletes a profile by their unique identifier. This action cannot be undone.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Profile deleted successfully",
              content = @Content
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Profile not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "User not found",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "PROFILE_NOT_FOUND",
                          "message": "Profile with id 1 does not exist",
                          "path": "/api/v1/profiles/1"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/profiles/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<Void> deleteProfile(
      @Parameter(description = "Profile ID to delete", example = "1", required = true)
      @PathVariable Long profileId
  ) {

    var deleteProfileCommand = new DeleteProfileCommand(profileId);

    profileCommandService.handle(deleteProfileCommand);

    return ResponseEntity
        .noContent()
        .build();
  }

  @GetMapping("/{profileId}")
  @Operation(
      summary = "Get profile by ID",
      description = "Retrieves a specific profile by their unique identifier. " +
          "Returns detailed profile information if found.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Profile found successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProfileResource.class),
                  examples = @ExampleObject(
                      name = "Profile found",
                      value = """
                        {
                          "id": 1,
                          "name": "John Doe",
                          "email": "john.doe@example.com"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Profile not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "Profile not found",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "PROFILE_NOT_FOUND",
                          "message": "Profile with id 1 does not exist",
                          "path": "/api/v1/profiles/1"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid profile ID format",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "Invalid ID format",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 400,
                          "error": "Bad Request",
                          "code": "INVALID_ID_FORMAT",
                          "message": "Invalid profile ID format",
                          "path": "/api/v1/profiles/invalid"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/profiles/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<ProfileResource> getProfileById(
      @Parameter(description = "Profile ID to retrieve", example = "1", required = true)
      @PathVariable Long profileId
  ) {

    var getProfileByQIdQuery = new GetProfileByIdQuery(profileId);

    var profileOptional = profileQueryService.handle(getProfileByQIdQuery);
    if (profileOptional.isEmpty()) {
      return ResponseEntity
          .notFound()
          .build();
    }

    var profileResource = ProfileResourceFromEntityAssembler
        .toResourceFromEntity(profileOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(profileResource);
  }

  @GetMapping("/email/{email}")
  @Operation(
      summary = "Get profile by email",
      description = "Retrieves a specific profile by their email address." +
          " Returns detailed profile information if found.",
      parameters = {
          @Parameter(
              name = "email",
              description = "Email address of the profile to retrieve",
              example = "john.doe@example.com",
              required = true,
              schema = @Schema(type = "string", format = "email")
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Profile found successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProfileResource.class),
                  examples = @ExampleObject(
                      name = "Profile found",
                      value = """
                        {
                          "id": 1,
                          "name": "John Doe",
                          "email": "john.doe@example.com"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid email format",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "Invalid email format",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 400,
                          "error": "Bad Request",
                          "code": "INVALID_EMAIL_FORMAT",
                          "message": "Invalid email format",
                          "path": "/api/v1/profiles/email/2"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Profile not found with the provided email",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "Profile not found",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "PROFILE_NOT_FOUND",
                          "message": "Profile with email 'john.doe@example.com' does not exist",
                          "path": "/api/v1/profiles/email/john.doe@example.com"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/profiles/email"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<ProfileResource> getProfileByEmail(
      @Parameter(description = "Email address to search", example = "john.doe@example.com", required = true)
      @PathVariable
      @Email(message = "Invalid email format")
      @NotBlank(message = "Email is required") String email
  ) {

    var getProfileByEmailQuery = new GetProfileByEmailQuery(email);

    var profileOptional = profileQueryService.handle(getProfileByEmailQuery);
    if (profileOptional.isEmpty()) {
      return ResponseEntity
          .notFound()
          .build();
    }

    var profileResource = ProfileResourceFromEntityAssembler
        .toResourceFromEntity(profileOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(profileResource);
  }

  @GetMapping("/name/{name}")
  @Operation(
      summary = "Get profile by name",
      description = "Retrieves a specific profile by their name. " +
          "Returns detailed profile information if found.",
      parameters = {
          @Parameter(
              name = "name",
              description = "Name of the profile to retrieve",
              example = "John Doe",
              required = true,
              schema = @Schema(type = "string")
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Profile found successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProfileResource.class),
                  examples = @ExampleObject(
                      name = "Profile found",
                      value = """
                        {
                          "id": 1,
                          "name": "John Doe",
                          "email": "john.doe@example.com"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid name format",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "Invalid name",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 400,
                          "error": "Bad Request",
                          "code": "INVALID_NAME",
                          "message": "Name cannot be blank",
                          "path": "/api/v1/profiles/name/"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Profile not found with the provided name",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      name = "Profile not found",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "PROFILE_NOT_FOUND",
                          "message": "Profile with name 'John Doe' does not exist",
                          "path": "/api/v1/profiles/name/John%20Doe"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/profiles/name/John%20Doe"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<ProfileResource> getProfileByName(
      @Parameter(description = "Name of the profile to retrieve",
          example = "John Doe",
          required = true)
      @PathVariable
      @NotBlank(message = "Name is required") String name
  ) {
    var getProfileByNameQuery = new GetProfileByNameQuery(name);

    var profileOptional = profileQueryService.handle(getProfileByNameQuery);
    if (profileOptional.isEmpty()) {
      return ResponseEntity
          .notFound()
          .build();
    }

    var profileResource = ProfileResourceFromEntityAssembler
        .toResourceFromEntity(profileOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(profileResource);
  }
}
