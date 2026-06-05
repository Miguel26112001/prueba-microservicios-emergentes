package com.example.ai.agent.infrastructure.clients.sales;

import com.example.ai.agent.domain.model.responses.OrderResource;
import com.example.ai.agent.domain.model.responses.ProductResource;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateOrderRequest;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateProductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "sales-service")
public interface SalesClient {

  // ==================== Product Endpoints ====================

  @PostMapping("/api/v1/products")
  ProductResource createProduct(
      @RequestBody CreateProductRequest request
  );

  @GetMapping("/api/v1/products")
  List<ProductResource> getAllProducts();

  @GetMapping("/api/v1/products/{productId}")
  ProductResource getProductById(
      @PathVariable Long productId
  );

  @GetMapping("/api/v1/products/name/{productName}")
  ProductResource getProductByName(
      @PathVariable String productName
  );

  @GetMapping("/api/v1/products/search/{name}")
  List<ProductResource> getProductsByRelatedName(
      @PathVariable String name
  );

  // ==================== Order Endpoints ====================
  
  @GetMapping("/api/v1/orders")
  List<OrderResource> getAllOrders();

  @GetMapping("/api/v1/orders/{orderId}")
  OrderResource getOrderById(
      @PathVariable Long orderId
  );

  @GetMapping("/api/v1/orders/user/{userId}")
  List<OrderResource> getOrdersByUserId(
      @PathVariable Long userId
  );

  @PostMapping("/api/v1/orders")
  OrderResource createOrder(
      @RequestBody CreateOrderRequest request
  );

  @DeleteMapping("/api/v1/orders/{orderId}")
  void deleteOrder(
      @PathVariable Long orderId
  );
}