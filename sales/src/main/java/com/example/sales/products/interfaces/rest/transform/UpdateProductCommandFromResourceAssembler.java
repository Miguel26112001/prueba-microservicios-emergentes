package com.example.sales.products.interfaces.rest.transform;

import com.example.sales.products.domain.model.commands.UpdateProductCommand;
import com.example.sales.products.interfaces.rest.resources.UpdateProductResource;

public class UpdateProductCommandFromResourceAssembler {

  private UpdateProductCommandFromResourceAssembler() {
  }

  public static UpdateProductCommand toCommandFromResource(
      Long productId,
      UpdateProductResource resource) {

    return new UpdateProductCommand(
        productId,
        resource.name(),
        resource.price(),
        resource.stock()
    );
  }
}
