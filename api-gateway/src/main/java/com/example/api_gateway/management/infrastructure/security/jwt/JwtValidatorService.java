package com.example.api_gateway.management.infrastructure.security.jwt;

import com.example.api_gateway.management.infrastructure.configuration.AuthorizationProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtValidatorService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(JwtValidatorService.class);

  private final AuthorizationProperties authorizationProperties;

  public JwtValidatorService(
      AuthorizationProperties authorizationProperties
  ) {

    this.authorizationProperties = authorizationProperties;
  }

  private SecretKey getSigningKey() {

    return Keys.hmacShaKeyFor(
        authorizationProperties.jwt()
            .secret()
            .getBytes(StandardCharsets.UTF_8)
    );
  }

  public boolean isInvalidToken(
      String token
  ) {

    try {

      Jwts.parser()
          .verifyWith(getSigningKey())
          .requireIssuer(
              authorizationProperties.jwt().issuer()
          )
          .requireAudience(
              authorizationProperties.jwt().audience()
          )
          .build()
          .parseSignedClaims(token);

      return false;

    } catch (SecurityException e) {

      LOGGER.warn("Invalid JWT signature");

    } catch (JwtException e) {

      LOGGER.warn("Invalid JWT: {}", e.getMessage());

    }

    return true;
  }

  public Claims getClaims(
      String token
  ) {

    return Jwts.parser()
        .verifyWith(getSigningKey())
        .requireIssuer(
            authorizationProperties.jwt().issuer()
        )
        .requireAudience(
            authorizationProperties.jwt().audience()
        )
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}