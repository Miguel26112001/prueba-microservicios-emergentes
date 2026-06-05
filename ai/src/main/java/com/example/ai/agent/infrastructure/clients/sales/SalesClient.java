package com.example.ai.agent.infrastructure.clients.sales;

import com.example.ai.agent.domain.model.responses.ProductResource;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateProductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "sales-service")
public interface SalesClient {

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
}