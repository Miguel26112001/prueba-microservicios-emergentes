package com.example.sales.products.domain.services;

import java.util.Optional;
import com.example.sales.products.domain.model.aggregates.Product;
import com.example.sales.products.domain.model.commands.CreateProductCommand;
import com.example.sales.products.domain.model.commands.DeleteProductCommand;
import com.example.sales.products.domain.model.commands.UpdateProductCommand;

public interface ProductCommandService {

  Optional<Product> handle(CreateProductCommand command);

  Optional<Product> handle(UpdateProductCommand command);

  void handle(DeleteProductCommand command);

}
