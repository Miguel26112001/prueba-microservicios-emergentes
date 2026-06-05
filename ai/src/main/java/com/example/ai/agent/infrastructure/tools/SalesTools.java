package com.example.ai.agent.infrastructure.tools;

import com.example.ai.agent.domain.model.responses.OrderResource;
import com.example.ai.agent.domain.model.responses.ProductResource;
import com.example.ai.agent.domain.model.responses.ToolResponse;
import com.example.ai.agent.infrastructure.clients.sales.SalesClient;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateOrderDetailRequest;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateOrderRequest;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateProductRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

  // ==================== Product Tools ====================

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

  // ==================== Order Tools ====================

  @Tool(description = "Obtiene todas las órdenes registradas")
  public ToolResponse<List<OrderResource>> getAllOrders() {

    try {

      List<OrderResource> orders = salesClient.getAllOrders();

      if (orders.isEmpty()) {
        return new ToolResponse<>(
            true,
            "ORDER_001",
            "No hay órdenes registradas",
            orders
        );
      }

      return new ToolResponse<>(
          true,
          "ORDER_001",
          "Órdenes obtenidas exitosamente",
          orders
      );

    } catch (Exception e) {

      log.error("Error obteniendo órdenes", e);

      return new ToolResponse<>(
          false,
          "ORDER_500",
          "Error interno al obtener órdenes",
          null
      );
    }
  }

  @Tool(description = "Obtiene una orden por su ID")
  public ToolResponse<OrderResource> getOrderById(Long orderId) {

    if (orderId == null || orderId <= 0) {
      return new ToolResponse<>(
          false,
          "ORDER_400",
          "ID de orden inválido",
          null
      );
    }

    try {

      OrderResource order = salesClient.getOrderById(orderId);

      return new ToolResponse<>(
          true,
          "ORDER_002",
          "Orden encontrada",
          order
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "ORDER_404",
          "No se encontró una orden con el ID: " + orderId,
          null
      );

    } catch (Exception e) {

      log.error("Error obteniendo orden por ID: {}", orderId, e);

      return new ToolResponse<>(
          false,
          "ORDER_500",
          "Error interno al buscar la orden",
          null
      );
    }
  }

  @Tool(description = "Obtiene todas las órdenes de un perfil específico por su ID")
  public ToolResponse<List<OrderResource>> getOrdersByProfileId(Long profileId) {

    if (profileId == null || profileId <= 0) {
      return new ToolResponse<>(
          false,
          "ORDER_400",
          "ID de perfil inválido",
          null
      );
    }

    try {

      List<OrderResource> orders = salesClient.getOrdersByUserId(profileId);

      if (orders.isEmpty()) {
        return new ToolResponse<>(
            true,
            "ORDER_003",
            "El perfil con ID " + profileId + " no tiene órdenes registradas",
            orders
        );
      }

      return new ToolResponse<>(
          true,
          "ORDER_003",
          "Se encontraron " + orders.size() + " orden(es) para el perfil",
          orders
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "ORDER_404",
          "Perfil no encontrado o sin órdenes",
          null
      );

    } catch (Exception e) {

      log.error("Error obteniendo órdenes del perfil: {}", profileId, e);

      return new ToolResponse<>(
          false,
          "ORDER_500",
          "Error interno al buscar órdenes del perfil",
          null
      );
    }
  }

  @Tool(description = "Crea una nueva orden para un usuario. " +
      "Debe proporcionar userId y una lista de items con productId y quantity")
  public ToolResponse<OrderResource> createOrder(
      Long profileId,
      List<CreateOrderDetailRequest> details
  ) {

    if (profileId == null || profileId <= 0) {
      return new ToolResponse<>(
          false,
          "ORDER_400",
          "ID de perfil inválido",
          null
      );
    }

    if (details == null || details.isEmpty()) {
      return new ToolResponse<>(
          false,
          "ORDER_400",
          "La orden debe tener al menos un item",
          null
      );
    }

    // Validar items
    for (CreateOrderDetailRequest detail : details) {
      if (detail.productId() == null || detail.productId() <= 0) {
        return new ToolResponse<>(
            false,
            "ORDER_400",
            "ID de producto inválido en los detalles",
            null
        );
      }
      if (detail.quantity() == null || detail.quantity() <= 0) {
        return new ToolResponse<>(
            false,
            "ORDER_400",
            "La cantidad debe ser mayor a 0",
            null
        );
      }
    }

    try {

      CreateOrderRequest request = new CreateOrderRequest(profileId, details);

      OrderResource order = salesClient.createOrder(request);

      return new ToolResponse<>(
          true,
          "ORDER_004",
          String.format("Orden creada exitosamente. ID: %d, Total: $%.2f", order.id(), order.total()),
          order
      );

    } catch (FeignException.BadRequest e) {

      return new ToolResponse<>(
          false,
          "ORDER_400",
          "Datos de orden inválidos. Verifique que los productos existan y tengan stock suficiente",
          null
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "ORDER_404",
          "Perfil o producto no encontrado",
          null
      );

    } catch (Exception e) {

      log.error("Error creando orden para perfil: {}", profileId, e);

      return new ToolResponse<>(
          false,
          "ORDER_500",
          "Error interno al crear la orden",
          null
      );
    }
  }

  @Tool(description = "Elimina una orden por su ID")
  public ToolResponse<Void> deleteOrder(Long orderId) {

    if (orderId == null || orderId <= 0) {
      return new ToolResponse<>(
          false,
          "ORDER_400",
          "ID de orden inválido",
          null
      );
    }

    try {

      salesClient.deleteOrder(orderId);

      return new ToolResponse<>(
          true,
          "ORDER_005",
          "Orden eliminada exitosamente",
          null
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "ORDER_404",
          "No se encontró una orden con el ID: " + orderId,
          null
      );

    } catch (Exception e) {

      log.error("Error eliminando orden: {}", orderId, e);

      return new ToolResponse<>(
          false,
          "ORDER_500",
          "Error interno al eliminar la orden",
          null
      );
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}