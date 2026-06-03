package com.example.authentication.management.interfaces.rest.exceptions;

import com.example.authentication.management.domain.exceptions.InvalidPasswordException;
import com.example.authentication.management.domain.exceptions.RoleNotFoundException;
import com.example.authentication.management.domain.exceptions.UserNotFoundException;
import com.example.authentication.shared.interfaces.rest.resources.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class DomainExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResource> handleUserNotFound(
      UserNotFoundException exception
  ) {

    return buildResponse(
        "USER_NOT_FOUND",
        exception.getMessage(),
        HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<ErrorResource> handleRoleNotFound(
      RoleNotFoundException exception
  ) {

    return buildResponse(
        "ROLE_NOT_FOUND",
        exception.getMessage(),
        HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResource> handleInvalidPassword(
      InvalidPasswordException exception
  ) {
    
    return buildResponse(
        "INVALID_PASSWORD",
        exception.getMessage(),
        HttpStatus.UNAUTHORIZED
    );
  }

  private ResponseEntity<ErrorResource> buildResponse(
      String error,
      String message,
      HttpStatus status) {

    return ResponseEntity.status(status)
        .body(new ErrorResource(
            error,
            message,
            status.value(),
            LocalDateTime.now()
        ));
  }
}