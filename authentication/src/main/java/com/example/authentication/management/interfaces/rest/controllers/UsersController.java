package com.example.authentication.management.interfaces.rest.controllers;

import com.example.authentication.management.domain.model.queries.GetUserByIdQuery;
import com.example.authentication.management.domain.services.UserQueryService;
import com.example.authentication.management.interfaces.rest.resources.UserResource;
import com.example.authentication.management.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is a REST controller that exposes the user's resource.
 * It includes the following operations:
 * - GET /api/v1/users: returns all the users
 * - GET /api/v1/users/{userId}: returns the user with the given id
 **/
@RestController
@RequestMapping(
    value = "/api/v1/users",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(
    name = "Users",
    description = "User Management Endpoints"
)
public class UsersController {

  private final UserQueryService userQueryService;

  public UsersController(
      UserQueryService userQueryService
  ) {

    this.userQueryService = userQueryService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResource> getUserById(
      @PathVariable Long userId
  ) {

    var getUserByIdQuery = new GetUserByIdQuery(userId);

    var user = userQueryService.getUserById(getUserByIdQuery);

    var resource = UserResourceFromEntityAssembler
        .toResourceFromEntity(user);

    return ResponseEntity.ok(resource);
  }
}