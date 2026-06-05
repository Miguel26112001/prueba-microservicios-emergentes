package com.example.authentication.management.domain.exceptions;

public class RoleNotFoundException extends RuntimeException {

  private RoleNotFoundException(String message) {
    super(message);
  }

  public static RoleNotFoundException withName(String name) {
    return new RoleNotFoundException("Role with name '" + name + "' not found");
  }
}