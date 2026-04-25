package com.example.sales.orders.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderResource(
  @NotNull(message = "User id is required")
  Long userId,

  @Valid
  @NotEmpty(message = "Order must contain at least one detail")
  List<CreateOrderDetailResource> details
) {
}
