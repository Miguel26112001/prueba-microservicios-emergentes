package com.example.sales.products.domain.model.aggregates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.example.sales.products.domain.model.commands.CreateProductCommand;
import com.example.sales.products.domain.model.commands.UpdateProductCommand;
import com.example.sales.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Product entity representing an item available for sale")
public class Product extends AuditableAbstractAggregateRoot<Product> {

  @Column(nullable = false, length = 150)
  @NotBlank(message = "Product name is required")
  @Size(min = 2, max = 150, message = "Product name must be between 2 and 150 characters")
  @Schema(
      description = "Product name",
      example = "Laptop Lenovo IdeaPad",
      minLength = 2,
      maxLength = 150,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @Column(nullable = false, precision = 10, scale = 2)
  @DecimalMin(value = "0.01", message = "Price must be greater than 0")
  @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
  @Schema(
      description = "Product price",
      example = "1499.99",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal price;

  @Column(nullable = false)
  @Min(value = 0, message = "Stock cannot be negative")
  @Schema(
      description = "Available stock quantity",
      example = "25",
      minimum = "0",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer stock;

  public Product(CreateProductCommand command) {
    this.name = command.name();
    this.price = command.price();
    this.stock = command.stock();
  }

  public void update(UpdateProductCommand command) {
    this.name = command.name();
    this.price = command.price();
    this.stock = command.stock();
  }
}
