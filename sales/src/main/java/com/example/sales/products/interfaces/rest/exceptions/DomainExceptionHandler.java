package com.example.sales.products.interfaces.rest.exceptions;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.sales.products.domain.exceptions.NameAlreadyExistsException;
import com.example.sales.products.domain.exceptions.ProductNotFoundException;
import com.example.sales.shared.interfaces.rest.resources.MessageResource;

@RestControllerAdvice
public class DomainExceptionHandler {

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<MessageResource> handleProductNotFoundException(
      ProductNotFoundException e,
      HttpServletRequest request) {

    MessageResource response = new MessageResource(
        LocalDateTime.now().toString(),
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        "PRODUCT_NOT_FOUND",
        e.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(response);
  }

  @ExceptionHandler(NameAlreadyExistsException.class)
  public ResponseEntity<MessageResource> handleNameAlreadyExistsException(
      NameAlreadyExistsException e,
      HttpServletRequest request) {

    MessageResource response = new MessageResource(
        LocalDateTime.now().toString(),
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        "PRODUCT_NAME_ALREADY_EXISTS",
        e.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(response);
  }

}
