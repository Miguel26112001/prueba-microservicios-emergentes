package com.example.users.information.domain.exceptions;

public class ProfileNotFoundException extends RuntimeException {

  public ProfileNotFoundException(String message) {
    super(message);
  }
}
