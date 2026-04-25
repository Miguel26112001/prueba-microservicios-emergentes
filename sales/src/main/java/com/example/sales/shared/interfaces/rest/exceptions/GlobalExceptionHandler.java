package com.example.sales.shared.interfaces.rest.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.sales.shared.interfaces.rest.resources.MessageResource;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<MessageResource> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e,
      HttpServletRequest request) {

    MessageResource error = new MessageResource(
        LocalDateTime.now().toString(),
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "VALIDATION_ERROR",
        e.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<MessageResource> handleConstraintViolationException(
      ConstraintViolationException e,
      HttpServletRequest request) {

    String errorMessage = e.getConstraintViolations().stream()
        .map(violation ->
            violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.joining(", "));

    MessageResource error = new MessageResource(
        LocalDateTime.now().toString(),
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "CONSTRAINT_VIOLATION",
        errorMessage,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(error);
  }
}
