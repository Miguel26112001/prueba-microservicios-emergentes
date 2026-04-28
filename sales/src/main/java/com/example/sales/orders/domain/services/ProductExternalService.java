package com.example.sales.orders.domain.services;

import com.example.sales.orders.domain.model.valueobjects.ProductData;

import java.math.BigDecimal;

public interface ProductExternalService {

  boolean existsProduct(Long productId);

  BigDecimal getProductPrice(Long productId);

  boolean hasStock(Long productId, Integer quantity);

  void reduceStock(Long productId, Integer quantity);

  void increaseStock(Long productId, Integer quantity);

  ProductData getProductData(Long productId);
}
