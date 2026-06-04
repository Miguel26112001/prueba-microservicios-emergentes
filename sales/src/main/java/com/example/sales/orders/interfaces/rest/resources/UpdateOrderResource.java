package com.example.sales.orders.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateOrderResource(
  @Valid
  @NotEmpty(message = "Order must contain at least one detail")
  List<UpdateOrderDetailResource> details
) {
}