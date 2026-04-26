package com.example.sales.orders.interfaces.rest.transform;

import com.example.sales.orders.domain.model.commands.OrderDetailCommand;
import com.example.sales.orders.domain.model.commands.UpdateOrderCommand;
import com.example.sales.orders.interfaces.rest.resources.UpdateOrderResource;

public class UpdateOrderCommandFromResourceAssembler {

  private UpdateOrderCommandFromResourceAssembler() {
  }

  public static UpdateOrderCommand toCommandFromResource(
      Long orderId,
      UpdateOrderResource resource
  ) {
    var details = resource.details().stream()
        .map(detail -> new OrderDetailCommand(
            detail.productId(),
            detail.quantity()
        ))
        .toList();

    return new UpdateOrderCommand(
        orderId,
        details
    );
  }
}