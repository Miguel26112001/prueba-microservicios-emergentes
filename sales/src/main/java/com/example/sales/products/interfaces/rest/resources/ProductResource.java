package com.example.sales.products.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Product resource representation", title = "Product")
public record ProductResource(

    @Schema(description = "Unique identifier of the product",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY,
        requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "Name of the product",
        example = "Laptop Lenovo IdeaPad",
        minLength = 2,
        maxLength = 150,
        requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(description = "Price of the product",
        example = "1499.99",
        minimum = "0.01",
        requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal price,

    @Schema(description = "Available stock quantity of the product",
        example = "25",
        minimum = "0",
        requiredMode = Schema.RequiredMode.REQUIRED)
    Integer stock
) {
}
