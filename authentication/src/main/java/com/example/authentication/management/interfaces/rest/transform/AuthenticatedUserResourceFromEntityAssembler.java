package com.example.authentication.management.interfaces.rest.transform;

import com.example.authentication.management.domain.model.aggregates.User;
import com.example.authentication.management.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {

  private AuthenticatedUserResourceFromEntityAssembler() {
  }

  public static AuthenticatedUserResource toResourceFromEntity(
      User entity,
      String token
  ) {

    return new AuthenticatedUserResource(
        entity.getId(),
        entity.getUsername(),
        token
    );
  }
}