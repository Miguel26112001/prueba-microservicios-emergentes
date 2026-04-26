package com.example.sales.orders.interfaces.rest.controllers;

import com.example.sales.orders.domain.model.commands.DeleteOrderCommand;
import com.example.sales.orders.domain.model.queries.GetAllOrdersQuery;
import com.example.sales.orders.domain.model.queries.GetOrderByIdQuery;
import com.example.sales.orders.domain.services.OrderCommandService;
import com.example.sales.orders.domain.services.OrderQueryService;
import com.example.sales.orders.interfaces.rest.resources.CreateOrderResource;
import com.example.sales.orders.interfaces.rest.resources.OrderResource;
import com.example.sales.orders.interfaces.rest.resources.UpdateOrderResource;
import com.example.sales.orders.interfaces.rest.transform.CreateOrderCommandFromResourceAssembler;
import com.example.sales.orders.interfaces.rest.transform.OrderResourceFromEntityAssembler;
import com.example.sales.orders.interfaces.rest.transform.UpdateOrderCommandFromResourceAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(
    value = "/api/v1/orders",
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class OrderController {

  private final OrderQueryService orderQueryService;
  private final OrderCommandService orderCommandService;

  public OrderController(
      OrderQueryService orderQueryService,
      OrderCommandService orderCommandService) {
    this.orderQueryService = orderQueryService;
    this.orderCommandService = orderCommandService;
  }

  @GetMapping
  public ResponseEntity<List<OrderResource>> getAllOrders() {

    var orders = orderQueryService.handle(new GetAllOrdersQuery());

    var resources = orders.stream()
        .map(OrderResourceFromEntityAssembler::toResourceFromEntity)
        .toList();

    if (resources.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(resources);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResource> getOrderById(
      @PathVariable Long orderId
  ) {

    var orderOptional = orderQueryService.handle(new GetOrderByIdQuery(orderId));

    if (orderOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var resource = OrderResourceFromEntityAssembler
        .toResourceFromEntity(orderOptional.get());

    return ResponseEntity.ok(resource);
  }

  @PostMapping
  public ResponseEntity<OrderResource> createOrder(
      @RequestBody @Valid CreateOrderResource resource
  ) {

    var command = CreateOrderCommandFromResourceAssembler
        .toCommandFromResource(resource);

    var orderOptional = orderCommandService.handle(command);

    if (orderOptional.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var response = OrderResourceFromEntityAssembler
        .toResourceFromEntity(orderOptional.get());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

  @PutMapping("/{orderId}")
  public ResponseEntity<OrderResource> updateOrder(
      @PathVariable Long orderId,
      @RequestBody @Valid UpdateOrderResource resource
  ) {

    var command = UpdateOrderCommandFromResourceAssembler
        .toCommandFromResource(orderId, resource);

    var orderOptional = orderCommandService.handle(command);

    if (orderOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var response = OrderResourceFromEntityAssembler
        .toResourceFromEntity(orderOptional.get());

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{orderId}")
  public ResponseEntity<Void> deleteOrder(
      @PathVariable Long orderId
  ) {

    orderCommandService.handle(new DeleteOrderCommand(orderId));

    return ResponseEntity.noContent().build();
  }
}
