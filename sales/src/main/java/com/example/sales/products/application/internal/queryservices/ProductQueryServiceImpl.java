package com.example.sales.products.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.sales.products.domain.exceptions.ProductNotFoundException;
import com.example.sales.products.domain.exceptions.ProductWithIdNotFoundException;
import com.example.sales.products.domain.exceptions.ProductWithNameNotFoundException;
import com.example.sales.products.domain.model.aggregates.Product;
import com.example.sales.products.domain.model.queries.GetAllProductsQuery;
import com.example.sales.products.domain.model.queries.GetProductByIdQuery;
import com.example.sales.products.domain.model.queries.GetProductByNameQuery;
import com.example.sales.products.domain.services.ProductQueryService;
import com.example.sales.products.infrastructure.persistence.jpa.repositories.ProductRepository;

@Service
public class ProductQueryServiceImpl implements ProductQueryService {

  private final ProductRepository productRepository;

  public ProductQueryServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public List<Product> handle(GetAllProductsQuery query) {
    return productRepository.findAll();
  }

  @Override
  public Optional<Product> handle(GetProductByIdQuery query) {

    var productOptional = productRepository.findById(query.productId());
    if (productOptional.isEmpty()) {
      throw new ProductWithIdNotFoundException(query.productId());
    }

    return productOptional;
  }

  @Override
  public Optional<Product> handle(GetProductByNameQuery query) {

    var productOptional = productRepository.findByName(query.name());
    if (productOptional.isEmpty()) {
      throw new ProductWithNameNotFoundException(query.name());
    }

    return productOptional;
  }
}
