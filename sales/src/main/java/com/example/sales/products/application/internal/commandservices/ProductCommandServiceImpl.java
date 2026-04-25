package com.example.sales.products.application.internal.commandservices;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.sales.products.domain.exceptions.NameAlreadyExistsException;
import com.example.sales.products.domain.exceptions.ProductWithIdNotFoundException;
import com.example.sales.products.domain.model.aggregates.Product;
import com.example.sales.products.domain.model.commands.CreateProductCommand;
import com.example.sales.products.domain.model.commands.DeleteProductCommand;
import com.example.sales.products.domain.model.commands.UpdateProductCommand;
import com.example.sales.products.domain.services.ProductCommandService;
import com.example.sales.products.infrastructure.persistence.jpa.repositories.ProductRepository;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

  private final ProductRepository productRepository;

  public ProductCommandServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Optional<Product> handle(CreateProductCommand command) {

    if (productRepository.existsByName(command.name())) {
      throw new NameAlreadyExistsException(command.name());
    }

    var newProduct = new Product(command);
    productRepository.save(newProduct);

    return productRepository.findByName(command.name());
  }

  @Override
  public Optional<Product> handle(UpdateProductCommand command) {

    var productOptional = productRepository.findById(command.productId());
    if (productOptional.isEmpty()) {
      throw new ProductWithIdNotFoundException(command.productId());
    }

    if (productRepository.existsByIdNotAndName(command.productId(), command.name())) {
      throw new NameAlreadyExistsException(command.name());
    }

    var productToUpdate = productOptional.get();
    productToUpdate.update(command);
    productRepository.save(productToUpdate);

    return Optional.of(productToUpdate);
  }

  @Override
  public void handle(DeleteProductCommand command) {
    var productOptional = productRepository.findById(command.productId());
    if (productOptional.isEmpty()) {
      throw new ProductWithIdNotFoundException(command.productId());
    }

    productRepository.delete(productOptional.get());
  }
}
