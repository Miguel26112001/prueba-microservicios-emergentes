package com.example.authentication.management.interfaces.rest.transform;

import com.example.authentication.management.domain.model.entities.Role;
import com.example.authentication.management.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {

  private RoleResourceFromEntityAssembler() {
  }

  public static RoleResource toResourceFromEntity(
      Role entity
  ) {

    return new RoleResource(
        entity.getId(),
        entity.getStringName()
    );
  }
}