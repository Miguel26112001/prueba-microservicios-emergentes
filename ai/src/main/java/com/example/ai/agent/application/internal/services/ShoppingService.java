package com.example.ai.agent.application.internal.services;

import com.example.ai.agent.domain.model.responses.*;
import com.example.ai.agent.infrastructure.clients.profiles.ProfilesClient;
import com.example.ai.agent.infrastructure.clients.sales.SalesClient;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateOrderDetailRequest;
import com.example.ai.agent.infrastructure.clients.sales.requests.CreateOrderRequest;
import com.example.ai.agent.infrastructure.clients.sales.requests.ProductRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShoppingService {

  private final ProfilesClient profilesClient;
  private final SalesClient salesClient;

  public ShoppingService(
      ProfilesClient profilesClient,
      SalesClient salesClient
  ) {
    this.profilesClient = profilesClient;
    this.salesClient = salesClient;
  }

  public ToolResponse<ShoppingResponse> createOrder(
      List<ProductRequest> requests
  ) {

    ProfilesResource profile;

    try {

      profile = profilesClient.getMyProfile();

    } catch (FeignException.Unauthorized e) {

      return new ToolResponse<>(
          false,
          "SHOPPING_401",
          "Debes iniciar sesión",
          null
      );
    }

    List<CreateOrderDetailRequest> details =
        new ArrayList<>();

    List<String> notFound =
        new ArrayList<>();

    List<ProductMatchResponse> ambiguous =
        new ArrayList<>();

    for (ProductRequest request : requests) {

      List<ProductResource> matches =
          salesClient.getProductsByRelatedName(
              request.searchTerm()
          );

      if (matches.isEmpty()) {

        notFound.add(
            request.searchTerm()
        );

        continue;
      }

      if (matches.size() > 1) {

        ambiguous.add(
            new ProductMatchResponse(
                request.searchTerm(),
                request.quantity(),
                matches
            )
        );

        continue;
      }

      ProductResource product =
          matches.getFirst();

      details.add(
          new CreateOrderDetailRequest(
              product.id(),
              request.quantity()
          )
      );
    }

    if (!notFound.isEmpty()) {

      return new ToolResponse<>(
          false,
          "SHOPPING_404",
          "Productos no encontrados",
          new ShoppingResponse(
              null,
              null,
              notFound
          )
      );
    }

    if (!ambiguous.isEmpty()) {

      return new ToolResponse<>(
          false,
          "SHOPPING_409",
          "Existen productos ambiguos",
          new ShoppingResponse(
              null,
              ambiguous,
              null
          )
      );
    }

    OrderResource order =
        salesClient.createOrder(
            new CreateOrderRequest(
                profile.id(),
                details
            )
        );

    return new ToolResponse<>(
        true,
        "SHOPPING_200",
        "Orden creada",
        new ShoppingResponse(
            order,
            null,
            null
        )
    );
  }
}