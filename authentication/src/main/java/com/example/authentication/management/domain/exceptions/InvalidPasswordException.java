package com.example.authentication.management.domain.exceptions;

public class InvalidPasswordException extends RuntimeException {

  private InvalidPasswordException(String message) {
    super(message);
  }

  public static InvalidPasswordException withMessage() {
    return new InvalidPasswordException("Invalid password.");
  }
}