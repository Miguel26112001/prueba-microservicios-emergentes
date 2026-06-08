package com.example.ai.agent.infrastructure.tools;

import com.example.ai.agent.application.internal.ProductExtractionService;
import com.example.ai.agent.application.internal.services.ShoppingService;
import com.example.ai.agent.domain.model.responses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShoppingTools {

  private final ShoppingService shoppingService;
  private final ProductExtractionService extractionService;

  public ShoppingTools(
      ShoppingService shoppingService,
      ProductExtractionService extractionService
  ) {

    this.shoppingService = shoppingService;
    this.extractionService = extractionService;
  }

  @Tool(description = """
      Crea una orden a partir de lenguaje natural.
      
      Ejemplos:
      - Quiero 2 laptops lenovo
      - Compra 3 teclados mecánicos
      - Haz un pedido de una lavadora samsung
      """)
  public ToolResponse<ShoppingResponse> quickPurchase(
      String description
  ) {

    if (description == null || description.isBlank()) {

      return new ToolResponse<>(
          false,
          "SHOPPING_400",
          "Descripción vacía",
          null
      );
    }

    var requests =
        extractionService.extract(
            description
        );

    if (requests.isEmpty()) {

      return new ToolResponse<>(
          false,
          "SHOPPING_400",
          "No pude identificar productos",
          null
      );
    }

    return shoppingService.createOrder(
        requests
    );
  }
}