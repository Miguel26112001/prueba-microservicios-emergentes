package com.example.users.information.domain.exceptions;

public class ProfileAlreadyExistsException extends RuntimeException {

  private ProfileAlreadyExistsException(String message) {
    super(message);
  }

  public static ProfileAlreadyExistsException withEmail(String email) {
    return new ProfileAlreadyExistsException("Profile with email '" + email + "' already exists");
  }

  public static ProfileAlreadyExistsException withUserId(Long userId) {
    return new ProfileAlreadyExistsException("Profile with user id '" + userId + "' already exists");
  }
}
