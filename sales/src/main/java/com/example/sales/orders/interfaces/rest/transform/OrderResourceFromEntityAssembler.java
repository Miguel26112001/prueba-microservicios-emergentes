package com.example.sales.orders.interfaces.rest.transform;

import com.example.sales.orders.domain.model.aggregates.Order;
import com.example.sales.orders.interfaces.rest.resources.OrderResource;

public class OrderResourceFromEntityAssembler {

  private OrderResourceFromEntityAssembler() {
  }

  public static OrderResource toResourceFromEntity(Order entity) {

    var detailResources = entity.getDetails().stream()
        .map(OrderDetailResourceFromEntityAssembler::toResourceFromEntity)
        .toList();

    return new OrderResource(
        entity.getId(),
        entity.getUserId(),
        entity.getOrderDate(),
        entity.getTotal(),
        detailResources
    );
  }
}
