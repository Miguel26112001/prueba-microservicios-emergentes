package com.example.users.information.interfaces.rest.controllers;

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
import com.example.users.information.domain.model.commands.DeleteUserCommand;
import com.example.users.information.domain.model.queries.GetAllUsersQuery;
import com.example.users.information.domain.model.queries.GetUserByEmailQuery;
import com.example.users.information.domain.model.queries.GetUserByIdQuery;
import com.example.users.information.domain.services.UserCommandService;
import com.example.users.information.domain.services.UserQueryService;
import com.example.users.information.interfaces.rest.resources.CreateUserResource;
import com.example.users.information.interfaces.rest.resources.UpdateUserResource;
import com.example.users.information.interfaces.rest.resources.UserResource;
import com.example.users.information.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import com.example.users.information.interfaces.rest.transform.UpdateUserCommandFromResourceAssembler;
import com.example.users.information.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.example.users.shared.interfaces.rest.resources.MessageResource;

@RestController
@RequestMapping(
    value = "/api/v1/users",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(
    name = "Users",
    description = "API for managing user resources. Provides endpoints to retrieve user information."
)
public class UserController {

  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;

  public UserController(UserQueryService userQueryService, UserCommandService userCommandService) {
    this.userQueryService = userQueryService;
    this.userCommandService = userCommandService;
  }

  @GetMapping
  @Operation(
      summary = "Get all users",
      description = "Retrieves a list of all registered users in the system. Returns an empty array if no users exist.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved users list",
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = UserResource.class)),
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
              description = "No users found",
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
                            "path": "/api/v1/users"
                          }
                          """
                  )
              )
          )
      }
  )
  public ResponseEntity<List<UserResource>> getAllUsers() {

    var users = userQueryService.handle(new GetAllUsersQuery());

    var userResources = users.stream()
        .map(UserResourceFromEntityAssembler::toResourceFromEntity)
        .toList();

    if (userResources.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(userResources);
  }

  @PostMapping
  @Operation(
      summary = "Create a new user",
      description = "Creates a new user with the provided information. The email must be unique and will be validated.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "User creation data",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CreateUserResource.class),
              examples = {
                  @ExampleObject(
                      name = "Valid user example",
                      summary = "Valid user data",
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
              description = "User created successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResource.class),
                  examples = @ExampleObject(
                      name = "Created user response",
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
                  schema = @Schema(implementation = MessageResource.class),
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
                              "path": "/api/v1/users"
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
                              "path": "/api/v1/users"
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "Duplicate email",
                      value = """
                        {
                          "timestamp": "2024-01-01T00:00:00",
                          "status": 409,
                          "error": "Conflict",
                          "code": "DUPLICATE_EMAIL",
                          "message": "Email 'john.doe@example.com' is already registered",
                          "path": "/api/v1/users"
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2024-01-01T00:00:00",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/users"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<UserResource> createUser(
      @RequestBody @Valid CreateUserResource resource
  ) {
    var createUserCommand = CreateUserCommandFromResourceAssembler.toCommandFromResource(resource);

    var userOptional = userCommandService.handle(createUserCommand);
    if (userOptional.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(null);
    }

    var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userResource);
  }

  @PutMapping("/{userId}")
  @Operation(
      summary = "Update an existing user",
      description = "Updates a user's information by their unique identifier. All fields are required for full update.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Updated user data",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UpdateUserResource.class),
              examples = {
                  @ExampleObject(
                      name = "Valid update example",
                      summary = "Complete user data update",
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
              description = "User updated successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResource.class),
                  examples = @ExampleObject(
                      name = "Updated user response",
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                            {
                              "timestamp": "2026-04-23T00:17:20.539",
                              "status": 400,
                              "error": "Validation Failed",
                              "code": "VALIDATION_ERROR",
                              "message": "Name is required, Email should be valid",
                              "path": "/api/v1/users/1"
                            }
                            """
                      )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                            {
                              "timestamp": "2026-04-23T00:17:20.539",
                              "status": 404,
                              "error": "Not Found",
                              "code": "USER_NOT_FOUND",
                              "message": "User with id 1 does not exist",
                              "path": "/api/v1/users/1"
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "Duplicate email",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 409,
                          "error": "Conflict",
                          "code": "DUPLICATE_EMAIL",
                          "message": "Email 'john.updated@example.com' is already registered by another user",
                          "path": "/api/v1/users/1"
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/users/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<UserResource> updateUser(
      @Parameter(description = "User ID to update", example = "1", required = true)
      @PathVariable Long userId,
      @RequestBody @Valid UpdateUserResource resource
  ) {

    var updateUserCommand = UpdateUserCommandFromResourceAssembler.toCommandFromResource(userId, resource);

    var userOptional = userCommandService.handle(updateUserCommand);
    if (userOptional.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(null);
    }

    var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userResource);
  }

  @DeleteMapping("/{userId}")
  @Operation(
      summary = "Delete a user",
      description = "Deletes a user by their unique identifier. This action cannot be undone.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "User deleted successfully",
              content = @Content
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "User not found",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "USER_NOT_FOUND",
                          "message": "User with id 1 does not exist",
                          "path": "/api/v1/users/1"
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/users/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "User ID to delete", example = "1", required = true)
      @PathVariable Long userId
  ) {

    var deleteUserCommand = new DeleteUserCommand(userId);

    userCommandService.handle(deleteUserCommand);

    return ResponseEntity
        .noContent()
        .build();
  }

  @GetMapping("/{userId}")
  @Operation(
      summary = "Get user by ID",
      description = "Retrieves a specific user by their unique identifier. Returns detailed user information if found.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "User found successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResource.class),
                  examples = @ExampleObject(
                      name = "User found",
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
              description = "User not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "User not found",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "USER_NOT_FOUND",
                          "message": "User with id 1 does not exist",
                          "path": "/api/v1/users/1"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid user ID format",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "Invalid ID format",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 400,
                          "error": "Bad Request",
                          "code": "INVALID_ID_FORMAT",
                          "message": "Invalid user ID format",
                          "path": "/api/v1/users/invalid"
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/users/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<UserResource> getUserById(
      @Parameter(description = "User ID to retrieve", example = "1", required = true)
      @PathVariable Long userId
  ) {

    var getUserByQIdQuery = new GetUserByIdQuery(userId);

    var userOptional = userQueryService.handle(getUserByQIdQuery);
    if (userOptional.isEmpty()) {
      return ResponseEntity
          .notFound()
          .build();
    }

    var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userResource);
  }

  @GetMapping("/email/{email}")
  @Operation(
      summary = "Get user by email",
      description = "Retrieves a specific user by their email address. Returns detailed user information if found.",
      parameters = {
          @Parameter(
              name = "email",
              description = "Email address of the user to retrieve",
              example = "john.doe@example.com",
              required = true,
              schema = @Schema(type = "string", format = "email")
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "User found successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResource.class),
                  examples = @ExampleObject(
                      name = "User found",
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "Invalid email format",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 400,
                          "error": "Bad Request",
                          "code": "INVALID_EMAIL_FORMAT",
                          "message": "Invalid email format",
                          "path": "/api/v1/users/email/2"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User not found with the provided email",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "User not found",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "USER_NOT_FOUND",
                          "message": "User with email 'john.doe@example.com' does not exist",
                          "path": "/api/v1/users/email/john.doe@example.com"
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
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/users/email"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<UserResource> getUserByEmail(
      @Parameter(description = "Email address to search", example = "john.doe@example.com", required = true)
      @PathVariable
      @Email(message = "Invalid email format")
      @NotBlank(message = "Email is required") String email
  ) {

    var getUserByEmailQuery = new GetUserByEmailQuery(email);

    var userOptional = userQueryService.handle(getUserByEmailQuery);
    if (userOptional.isEmpty()) {
      return ResponseEntity
          .notFound()
          .build();
    }

    var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userResource);
  }
}
