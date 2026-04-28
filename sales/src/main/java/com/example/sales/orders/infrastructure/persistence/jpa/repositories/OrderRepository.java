package com.example.sales.orders.infrastructure.persistence.jpa.repositories;

import com.example.sales.orders.domain.model.aggregates.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByUserId(Long userId);

  boolean existsByIdAndUserId(Long id, Long userId);

  boolean existsByUserId(Long userId);

  long countByUserId(Long userId);

  List<Order> findByOrderDateBetween(
    LocalDateTime startDate,
    LocalDateTime endDate
  );

  List<Order> findByTotalGreaterThanEqualOrderByTotalDesc(
    BigDecimal amount
  );

  void deleteByUserId(Long userId);
}
