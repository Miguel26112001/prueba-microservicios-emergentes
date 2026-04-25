package com.example.users.information.domain.exceptions;

public class UserWithIdNotFoundException extends UserNotFoundException {
  public UserWithIdNotFoundException(Long userId) {
    super("User with id '" + userId + "' not found");
  }
}
