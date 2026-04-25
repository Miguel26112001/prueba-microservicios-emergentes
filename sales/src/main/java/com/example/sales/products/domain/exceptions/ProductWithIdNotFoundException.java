package com.example.sales.products.domain.exceptions;

public class ProductWithIdNotFoundException extends ProductNotFoundException {
  public ProductWithIdNotFoundException(Long productId) {
    super("Product with id '" + productId + "' not found");
  }
}
