package com.example.authentication.management.interfaces.rest.transform;

import com.example.authentication.management.domain.model.commands.SignUpCommand;
import com.example.authentication.management.domain.model.entities.Role;
import com.example.authentication.management.interfaces.rest.resources.SignUpResource;

import java.util.ArrayList;

public class SignUpCommandFromResourceAssembler {

  private SignUpCommandFromResourceAssembler() {
  }

  public static SignUpCommand toCommandFromResource(
      SignUpResource resource
  ) {

    var roles = resource.roles() != null
        ? resource.roles().stream().map(Role::toRoleFromName).toList()
        : new ArrayList<Role>();

    return new SignUpCommand(
        resource.username(),
        resource.password(),
        resource.name(),
        resource.email(),
        roles
    );
  }
}