package com.example.ai.agent.infrastructure.tools;

import com.example.ai.agent.domain.model.responses.ProductResource;
import com.example.ai.agent.domain.model.responses.ToolResponse;
import com.example.ai.agent.infrastructure.clients.sales.SalesClient;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateProductRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class SalesTools {

  private final SalesClient salesClient;

  public SalesTools(
      SalesClient salesClient
  ) {

    this.salesClient = salesClient;
  }

  @Tool(description = "Obtiene todos los productos registrados")
  public ToolResponse<List<ProductResource>> getAllProducts() {

    try {

      List<ProductResource> products =
          salesClient.getAllProducts();

      return new ToolResponse<>(
          true,
          "PRODUCT_001",
          "Productos obtenidos",
          products
      );

    } catch (Exception e) {

      log.error("Error obteniendo productos", e);

      return new ToolResponse<>(
          false,
          "PRODUCT_500",
          "Error interno",
          null
      );
    }
  }

  @Tool(description = "Crea un nuevo producto")
  public ToolResponse<ProductResource> createProduct(
      String name,
      BigDecimal price,
      Integer stock
  ) {

    if (isBlank(name)) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "Nombre requerido",
          null
      );
    }

    if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "Precio inválido",
          null
      );
    }

    if (stock == null || stock < 0) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "Stock inválido",
          null
      );
    }

    try {

      ProductResource product =
          salesClient.createProduct(
              new CreateProductRequest(
                  name.trim(),
                  price,
                  stock
              )
          );

      return new ToolResponse<>(
          true,
          "PRODUCT_002",
          "Producto creado",
          product
      );

    } catch (FeignException.Conflict e) {

      return new ToolResponse<>(
          false,
          "PRODUCT_409",
          "Ya existe un producto con esos datos",
          null
      );

    } catch (FeignException.BadRequest e) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "Datos inválidos",
          null
      );

    } catch (Exception e) {

      log.error("Error creando producto", e);

      return new ToolResponse<>(
          false,
          "PRODUCT_500",
          "Error interno",
          null
      );
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}