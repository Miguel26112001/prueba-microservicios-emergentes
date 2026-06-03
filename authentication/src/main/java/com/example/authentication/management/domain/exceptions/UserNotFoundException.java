package com.example.authentication.management.domain.exceptions;

public class UserNotFoundException extends RuntimeException {

  private UserNotFoundException(String message) {
    super(message);
  }

  public static UserNotFoundException withId(Long id) {
    return new UserNotFoundException("User with id '" + id + "' not found");
  }
}
