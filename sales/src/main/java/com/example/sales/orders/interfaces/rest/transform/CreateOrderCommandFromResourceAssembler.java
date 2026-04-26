package com.example.sales.orders.interfaces.rest.transform;

import com.example.sales.orders.domain.model.commands.CreateOrderCommand;
import com.example.sales.orders.domain.model.commands.OrderDetailCommand;
import com.example.sales.orders.interfaces.rest.resources.CreateOrderResource;

public class CreateOrderCommandFromResourceAssembler {

  private CreateOrderCommandFromResourceAssembler() {
  }

  public static CreateOrderCommand toCommandFromResource(
      CreateOrderResource resource
  ) {
    var details = resource.details().stream()
        .map(detail -> new OrderDetailCommand(
            detail.productId(),
            detail.quantity()
        ))
        .toList();

    return new CreateOrderCommand(
        resource.userId(),
        details
    );
  }
}