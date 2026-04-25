package com.example.sales.products.interfaces.rest.transform;

import com.example.sales.products.domain.model.aggregates.Product;
import com.example.sales.products.interfaces.rest.resources.ProductResource;

public class ProductResourceFromEntityAssembler {

  private ProductResourceFromEntityAssembler() {
  }

  public static ProductResource toResourceFromEntity(Product product) {

    return new ProductResource(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getStock()
    );
  }

}
