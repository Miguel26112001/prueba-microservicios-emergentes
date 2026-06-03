package com.example.authentication.management.domain.exceptions;

import com.example.authentication.management.domain.model.entities.Role;

public class RoleNotFoundException extends RuntimeException {

  private RoleNotFoundException(String message) {
    super(message);
  }

  public static RoleNotFoundException withName(String name) {
    return new RoleNotFoundException("Role with name '" + name + "' not found");
  }
}
