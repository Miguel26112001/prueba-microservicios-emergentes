package com.example.sales.orders.interfaces.rest.transform;

import com.example.sales.orders.domain.model.entities.OrderDetail;
import com.example.sales.orders.interfaces.rest.resources.OrderDetailResource;

public class OrderDetailResourceFromEntityAssembler {

  private OrderDetailResourceFromEntityAssembler() {
  }

  public static OrderDetailResource toResourceFromEntity(
      OrderDetail entity
  ) {
    return new OrderDetailResource(
        entity.getId(),
        entity.getProductId(),
        entity.getQuantity(),
        entity.getSubtotal()
    );
  }
}