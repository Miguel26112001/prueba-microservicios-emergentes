package com.example.api_gateway.management.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "authorization")
public record AuthorizationProperties(
    Jwt jwt,
    List<String> publicPaths
) {

  public record Jwt(
      String secret,
      String issuer,
      String audience
  ) {
  }
}