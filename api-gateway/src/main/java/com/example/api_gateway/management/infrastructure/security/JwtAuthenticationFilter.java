package com.example.api_gateway.management.infrastructure.security;

import com.example.api_gateway.management.infrastructure.configuration.AuthorizationProperties;
import com.example.api_gateway.management.infrastructure.security.jwt.JwtValidatorService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtValidatorService jwtValidatorService;
  private final AuthorizationProperties authorizationProperties;

  private final AntPathMatcher matcher =
      new AntPathMatcher();

  public JwtAuthenticationFilter(
      JwtValidatorService jwtValidatorService,
      AuthorizationProperties authorizationProperties
  ) {

    this.jwtValidatorService = jwtValidatorService;
    this.authorizationProperties = authorizationProperties;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    String path = request.getRequestURI();

    boolean isPublic =
        authorizationProperties.publicPaths()
            .stream()
            .anyMatch(pattern ->
                matcher.match(pattern, path));

    if (isPublic) {

      filterChain.doFilter(request, response);
      return;
    }

    String authorization =
        request.getHeader("Authorization");

    if (authorization == null ||
        !authorization.startsWith("Bearer ")) {

      response.sendError(
          HttpServletResponse.SC_UNAUTHORIZED,
          "Missing authorization header"
      );

      return;
    }

    String token =
        authorization.substring(7);

    if (jwtValidatorService.isInvalidToken(token)) {

      response.sendError(
          HttpServletResponse.SC_UNAUTHORIZED,
          "Invalid token"
      );

      return;
    }

    Claims claims =
        jwtValidatorService.getClaims(token);

    request.setAttribute(
        "userId",
        claims.get("user_id")
    );

    request.setAttribute(
        "username",
        claims.getSubject()
    );

    request.setAttribute(
        "roles",
        claims.get("roles")
    );

    filterChain.doFilter(request, response);
  }
}