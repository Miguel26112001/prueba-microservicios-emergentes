package com.example.authentication.management.interfaces.rest.transform;

import com.example.authentication.management.domain.model.aggregates.User;
import com.example.authentication.management.domain.model.entities.Role;
import com.example.authentication.management.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

  private UserResourceFromEntityAssembler() {
  }

  public static UserResource toResourceFromEntity(
      User entity
  ) {

    var roles = entity.getRoles().stream()
        .map(Role::getStringName)
        .toList();

    return new UserResource(
        entity.getId(),
        entity.getUsername(),
        roles
    );
  }
}