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

  @Tool(description = "Obtiene un producto por su ID exacto")
  public ToolResponse<ProductResource> getProductById(Long productId) {

    if (productId == null || productId <= 0) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "ID de producto inválido",
          null
      );
    }

    try {

      ProductResource product = salesClient.getProductById(productId);

      return new ToolResponse<>(
          true,
          "PRODUCT_003",
          "Producto obtenido por ID",
          product
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "PRODUCT_404",
          "No se encontró un producto con el ID: " + productId,
          null
      );

    } catch (FeignException.BadRequest e) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "ID de producto inválido",
          null
      );

    } catch (Exception e) {

      log.error("Error obteniendo producto por ID: {}", productId, e);

      return new ToolResponse<>(
          false,
          "PRODUCT_500",
          "Error interno al buscar el producto",
          null
      );
    }
  }

  @Tool(description = "Obtiene un producto por su nombre exacto (coincidencia exacta)")
  public ToolResponse<ProductResource> getProductByName(String productName) {

    if (isBlank(productName)) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "Nombre de producto requerido",
          null
      );
    }

    try {

      ProductResource product = salesClient.getProductByName(productName.trim());

      return new ToolResponse<>(
          true,
          "PRODUCT_004",
          "Producto obtenido por nombre exacto",
          product
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "PRODUCT_404",
          "No se encontró un producto con el nombre exacto: '" + productName + "'",
          null
      );

    } catch (FeignException.BadRequest e) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "Nombre de producto inválido",
          null
      );

    } catch (Exception e) {

      log.error("Error obteniendo producto por nombre: {}", productName, e);

      return new ToolResponse<>(
          false,
          "PRODUCT_500",
          "Error interno al buscar el producto",
          null
      );
    }
  }

  @Tool(description = "Busca productos por nombre relacionado (búsqueda parcial, case-insensitive). " +
      "Ejemplo: 'teclado' encontrará 'Teclado Logitech', 'Teclado Mecánico', etc.")
  public ToolResponse<List<ProductResource>> getProductsByRelatedName(String searchTerm) {

    if (isBlank(searchTerm)) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "Término de búsqueda requerido",
          null
      );
    }

    try {

      List<ProductResource> products = salesClient.getProductsByRelatedName(searchTerm.trim());

      if (products.isEmpty()) {

        return new ToolResponse<>(
            true,
            "PRODUCT_005",
            "No se encontraron productos relacionados con: '" + searchTerm + "'",
            products
        );
      }

      return new ToolResponse<>(
          true,
          "PRODUCT_005",
          "Se encontraron " + products.size() + " producto(s) relacionado(s) con: '" + searchTerm + "'",
          products
      );

    } catch (FeignException.BadRequest e) {

      return new ToolResponse<>(
          false,
          "PRODUCT_400",
          "Término de búsqueda inválido",
          null
      );

    } catch (Exception e) {

      log.error("Error buscando productos por término: {}", searchTerm, e);

      return new ToolResponse<>(
          false,
          "PRODUCT_500",
          "Error interno al buscar productos",
          null
      );
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}