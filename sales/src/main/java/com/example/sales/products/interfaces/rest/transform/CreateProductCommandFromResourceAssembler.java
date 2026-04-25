package com.example.sales.products.interfaces.rest.transform;

import com.example.sales.products.domain.model.commands.CreateProductCommand;
import com.example.sales.products.interfaces.rest.resources.CreateProductResource;

public class CreateProductCommandFromResourceAssembler {

  private CreateProductCommandFromResourceAssembler() {
  }

  public static CreateProductCommand toCommandFromResource(CreateProductResource resource) {

    return new CreateProductCommand(
        resource.name(),
        resource.price(),
        resource.stock()
    );
  }
}
