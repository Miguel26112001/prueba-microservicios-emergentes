package com.example.sales.orders.domain.model.entities;

import com.example.sales.orders.domain.model.aggregates.Order;
import com.example.sales.shared.domain.model.entities.AuditableModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Order detail entity representing a product line inside an order")
public class OrderDetail extends AuditableModel {

  @Column(name = "product_id", nullable = false)
  @NotNull(message = "Product id is required")
  @Schema(
    description = "Referenced product identifier",
    example = "1",
    requiredMode = Schema.RequiredMode.REQUIRED)
  private Long productId;

  @Column(nullable = false)
  @NotNull(message = "Quantity is required")
  @Min(value = 1, message = "Quantity must be at least 1")
  @Schema(
    description = "Requested quantity of the product",
    example = "2",
    minimum = "1",
    requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer quantity;

  @Column(nullable = false, precision = 10, scale = 2)
  @NotNull(message = "Subtotal is required")
  @DecimalMin(value = "0.00", inclusive = true, message = "Subtotal cannot be negative")
  @Digits(integer = 8, fraction = 2, message = "Subtotal format is invalid")
  @Schema(
    description = "Subtotal amount for this line (price x quantity)",
    example = "2999.98",
    requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal subtotal;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  @Schema(description = "Parent order reference")
  private Order order;
}
