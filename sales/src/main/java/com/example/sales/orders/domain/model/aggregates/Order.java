package com.example.sales.orders.domain.model.aggregates;

import com.example.sales.orders.domain.model.entities.OrderDetail;
import com.example.sales.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Order aggregate representing a customer purchase")
public class Order extends AuditableAbstractAggregateRoot<Order> {

  @Column(name = "user_id", nullable = false)
  @NotNull(message = "User id is required")
  @Schema(
    description = "Identifier of the user who placed the order",
    example = "1",
    requiredMode = Schema.RequiredMode.REQUIRED)
  private Long userId;

  @Column(name = "order_date", nullable = false)
  @NotNull(message = "Order date is required")
  @Schema(
    description = "Date and time when the order was created",
    example = "2026-04-25T14:30:00",
    requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime orderDate;

  @Column(nullable = false, precision = 12, scale = 2)
  @NotNull(message = "Total is required")
  @DecimalMin(value = "0.00", message = "Total cannot be negative")
  @Digits(integer = 10, fraction = 2, message = "Total format is invalid")
  @Schema(
    description = "Total amount of the order",
    example = "1559.89",
    requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal total;

  @OneToMany(
    mappedBy = "order",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
  )
  @Builder.Default
  @Size(min = 1, message = "Order must contain at least one detail")
  @Schema(description = "List of products included in the order")
  private List<OrderDetail> details = new ArrayList<>();

  public void addDetail(OrderDetail detail) {
    detail.setOrder(this);
    this.details.add(detail);
  }

  public void removeDetail(OrderDetail detail) {
    this.details.remove(detail);
    detail.setOrder(null);
  }

  public void calculateTotal() {
    this.total = details.stream()
      .map(OrderDetail::getSubtotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
