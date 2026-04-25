package com.example.users.information.interfaces.rest.exceptions;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.users.information.domain.exceptions.EmailAlreadyExistsException;
import com.example.users.information.domain.exceptions.UserNotFoundException;
import com.example.users.information.domain.exceptions.UserWithIdNotFoundException;
import com.example.users.shared.interfaces.rest.resources.MessageResource;

@RestControllerAdvice
public class DomainExceptionHandler {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<MessageResource> handleEmailAlreadyExistsException(
      EmailAlreadyExistsException e,
      HttpServletRequest request) {

    MessageResource response = new MessageResource(
        LocalDateTime.now().toString(),
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        "EMAIL_ALREADY_EXISTS",
        e.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(response);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<MessageResource> handleUserNotFoundException(
      UserNotFoundException e,
      HttpServletRequest request) {

    MessageResource response = new MessageResource(
        LocalDateTime.now().toString(),
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        "USER_NOT_FOUND",
        e.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(response);
  }
}
