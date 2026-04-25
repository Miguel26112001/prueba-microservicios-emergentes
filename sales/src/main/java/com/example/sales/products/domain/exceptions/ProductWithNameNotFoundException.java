package com.example.sales.products.domain.exceptions;

public class ProductWithNameNotFoundException extends ProductNotFoundException {
  public ProductWithNameNotFoundException(String name) {
    super("Product with name '" + name + "' not found");
  }
}
