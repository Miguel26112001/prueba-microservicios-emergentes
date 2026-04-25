package com.example.users.information.domain.exceptions;

public class UserWithEmailNotFoundException extends UserNotFoundException {
  public UserWithEmailNotFoundException(String email) {
    super("User with email '" + email + "' not found");
  }
}
