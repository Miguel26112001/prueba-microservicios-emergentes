package com.example.users.information.domain.exceptions;

public class InvalidUserIdException extends RuntimeException {

  private InvalidUserIdException(String message) {
    super(message);
  }

  public static InvalidUserIdException forId(
      Long userId
  ) {

    return new InvalidUserIdException(
        String.format(
            "Invalid UserId: '%s'. Value must be a positive number greater than 0.",
            userId
        )
    );
  }
}
