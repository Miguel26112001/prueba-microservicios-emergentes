package com.example.sales.products.domain.exceptions;

public class NameAlreadyExistsException extends RuntimeException {
  public NameAlreadyExistsException(String name) {
    super("Product with name '" + name + "' already exists");
  }
}
