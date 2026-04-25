package com.example.sales.products.interfaces.acl;

import com.example.sales.products.domain.model.aggregates.Product;
import com.example.sales.products.infrastructure.persistence.jpa.repositories.ProductRepository;
import org.springframework.stereotype.Service;

/**
 * Facade exposed by Products bounded context
 * to be consumed internally by other bounded contexts (Orders, etc.)
 */
@Service
public class ProductsContextFacade {

  private final ProductRepository productRepository;

  public ProductsContextFacade(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Returns product or throws if it does not exist
   */
  public Product getProductById(Long productId) {
    return productRepository.findById(productId)
      .orElseThrow(() ->
        new RuntimeException("Product with id %d not found".formatted(productId))
      );
  }

  /**
   * Checks whether a product exists
   */
  public boolean existsProduct(Long productId) {
    return productRepository.existsById(productId);
  }

  /**
   * Checks if enough stock exists
   */
  public boolean hasEnoughStock(Long productId, Integer quantity) {
    var product = getProductById(productId);
    return product.getStock() >= quantity;
  }

  /**
   * Decrease stock after order creation
   */
  public void decreaseStock(Long productId, Integer quantity) {
    var product = getProductById(productId);

    if (product.getStock() < quantity) {
      throw new RuntimeException(
        "Insufficient stock for product id %d".formatted(productId)
      );
    }

    product.setStock(product.getStock() - quantity);
    productRepository.save(product);
  }

  /**
   * Increase stock if order is canceled or reverted
   */
  public void increaseStock(Long productId, Integer quantity) {
    var product = getProductById(productId);

    product.setStock(product.getStock() + quantity);
    productRepository.save(product);
  }

}
