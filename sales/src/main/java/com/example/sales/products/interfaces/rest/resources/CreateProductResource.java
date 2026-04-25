package com.example.sales.products.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Resource for creating a new product")
public record CreateProductResource(

    @Schema(
        description = "Name of the product",
        example = "Laptop Lenovo IdeaPad",
        minLength = 2,
        maxLength = 150,
        requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name is required")
    @Size(
        min = 2,
        max = 150,
        message = "Product name must be between 2 and 150 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9áéíóúñÑ\\s]+$",
        message = "Product name can only contain letters, numbers and spaces")
    String name,

    @Schema(
        description = "Price of the product",
        example = "1499.99",
        minimum = "0.01",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Price is required")
    @DecimalMin(
        value = "0.01",
        message = "Price must be greater than 0")
    @Digits(
        integer = 8,
        fraction = 2,
        message = "Price must have up to 8 integer digits and 2 decimal places")
    BigDecimal price,

    @Schema(
        description = "Initial stock quantity of the product",
        example = "25",
        minimum = "0",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Stock is required")
    @Min(
        value = 0,
        message = "Stock cannot be negative")
    Integer stock
) {
}
