package com.example.ai.agent.infrastructure.tools;

import com.example.ai.agent.domain.model.responses.OrderResource;
import com.example.ai.agent.domain.model.responses.ProductResource;
import com.example.ai.agent.domain.model.responses.ProfilesResource;
import com.example.ai.agent.domain.model.responses.ToolResponse;
import com.example.ai.agent.infrastructure.clients.profiles.ProfilesClient;
import com.example.ai.agent.infrastructure.clients.sales.SalesClient;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateOrderDetailRequest;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateOrderRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ShoppingTools {

  private final ProfilesClient profilesClient;
  private final SalesClient salesClient;

  public ShoppingTools(
      ProfilesClient profilesClient,
      SalesClient salesClient
  ) {

    this.profilesClient = profilesClient;
    this.salesClient = salesClient;
  }

  @Tool(description = """
        Crea una orden automáticamente a partir de una descripción natural.
        
        EJEMPLOS DE USO:
        - "Quiero 2 laptops lenovo y 3 lavadoras samsung"
        - "Compra 1 teclado mecánico y 2 mouse inalámbricos"
        - "Haz un pedido de una laptop acer y una lavadora lg"
        
        IMPORTANTE: Esta herramienta busca los productos, obtiene tu perfil y crea la orden en un solo paso.
        """)
  public ToolResponse<OrderResource> quickPurchase(String description) {

    log.info("Procesando compra rápida: {}", description);

    try {
      // Paso 1: Obtener perfil autenticado
      ProfilesResource profile;
      try {
        profile = profilesClient.getMyProfile();
        log.info("Perfil obtenido: ID={}", profile.id());
      } catch (FeignException.Unauthorized e) {
        return new ToolResponse<>(
            false,
            "SHOPPING_401",
            "No estás autenticado. Por favor inicia sesión primero.",
            null
        );
      } catch (Exception e) {
        return new ToolResponse<>(
            false,
            "SHOPPING_500",
            "Error al obtener tu perfil: " + e.getMessage(),
            null
        );
      }

      // Paso 2: Extraer productos y cantidades del texto
      List<ProductRequest> productRequests = extractProductsFromText(description);

      if (productRequests.isEmpty()) {
        return new ToolResponse<>(
            false,
            "SHOPPING_400",
            "No pude entender qué productos quieres comprar. Ejemplo: '2 laptops lenovo y 3 lavadoras'",
            null
        );
      }

      // Paso 3: Buscar cada producto
      List<CreateOrderDetailRequest> details = new ArrayList<>();
      List<String> notFound = new ArrayList<>();

      for (ProductRequest req : productRequests) {
        log.info("Buscando producto: {}", req.searchTerm);
        List<ProductResource> products = salesClient.getProductsByRelatedName(req.searchTerm);

        if (products.isEmpty()) {
          notFound.add(req.searchTerm);
        } else {
          // Tomar el primer producto que coincida
          ProductResource product = products.getFirst();
          details.add(new CreateOrderDetailRequest(product.id(), req.quantity));
          log.info("Producto encontrado: ID={}, Nombre={}, Cantidad={}",
              product.id(), product.name(), req.quantity);
        }
      }

      // Paso 4: Verificar si faltaron productos
      if (!notFound.isEmpty()) {
        return new ToolResponse<>(
            false,
            "SHOPPING_404",
            "No encontré estos productos: " + String.join(", ", notFound),
            null
        );
      }

      // Paso 5: Crear la orden
      CreateOrderRequest orderRequest = new CreateOrderRequest(profile.id(), details);
      OrderResource order = salesClient.createOrder(orderRequest);

      log.info("Orden creada: ID={}, Total={}", order.id(), order.total());

      return new ToolResponse<>(
          true,
          "SHOPPING_200",
          String.format("¡Orden creada exitosamente! ID: %d, Total: $%.2f", order.id(), order.total()),
          order
      );

    } catch (FeignException.BadRequest e) {
      return new ToolResponse<>(
          false,
          "SHOPPING_400",
          "Error en la orden: Verifica que los productos tengan stock suficiente",
          null
      );
    } catch (Exception e) {
      log.error("Error en compra rápida", e);
      return new ToolResponse<>(
          false,
          "SHOPPING_500",
          "Error interno al procesar tu compra: " + e.getMessage(),
          null
      );
    }
  }

  private List<ProductRequest> extractProductsFromText(String text) {
    List<ProductRequest> requests = new ArrayList<>();

    // Patrón para detectar "cantidad producto"
    // Ejemplos: "2 laptops lenovo", "3 lavadoras samsung", "1 teclado"
    Pattern pattern = Pattern.compile("(\\d+)\\s+([a-zA-Záéíóúñ\\s]+?)(?=\\d|$|y|,)");
    Matcher matcher = pattern.matcher(text.toLowerCase());

    while (matcher.find()) {
      int quantity = Integer.parseInt(matcher.group(1));
      String productName = matcher.group(2).trim();
      requests.add(new ProductRequest(productName, quantity));
    }

    // Si no encontró cantidades, asumir cantidad 1 para cada producto mencionado
    if (requests.isEmpty()) {
      Pattern simplePattern = Pattern.compile("(?:una?|un)\\s+([a-zA-Záéíóúñ\\s]+?)(?=\\s+y\\s+|\\s+y$|$)");
      Matcher simpleMatcher = simplePattern.matcher(text.toLowerCase());
      while (simpleMatcher.find()) {
        String productName = simpleMatcher.group(1).trim();
        requests.add(new ProductRequest(productName, 1));
      }
    }

    return requests;
  }

  private record ProductRequest(String searchTerm, int quantity) {}
}