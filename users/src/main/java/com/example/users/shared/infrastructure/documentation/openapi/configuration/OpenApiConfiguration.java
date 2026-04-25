package com.example.users.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Bean
  public OpenAPI iamPlatformOpenAPI() {
    return new OpenAPI()
        .info(apiInfo())
        .externalDocs(externalDocs())
        .addSecurityItem(new SecurityRequirement());
  }

  private Info apiInfo() {
    return new Info()
        .title("Users Platform API")
        .description("Users Platform Application REST API Documentation.")
        .version("v1.0.0")
        .license(new License()
            .name("Apache 2.0")
            .url("https://springdoc.org"));
  }

  private ExternalDocumentation externalDocs() {
    return new ExternalDocumentation()
        .description("Users Platform Documentation")
        .url("https://example.com");
  }
}