package com.example.sales.products.domain.services;

import java.util.List;
import java.util.Optional;
import com.example.sales.products.domain.model.aggregates.Product;
import com.example.sales.products.domain.model.queries.GetAllProductsQuery;
import com.example.sales.products.domain.model.queries.GetProductByIdQuery;
import com.example.sales.products.domain.model.queries.GetProductByNameQuery;

public interface ProductQueryService {

  List<Product> handle(GetAllProductsQuery query);

  Optional<Product> handle(GetProductByIdQuery query);

  Optional<Product> handle(GetProductByNameQuery query);

}
