package com.example.sales.orders.infrastructure.persistence.jpa.repositories;

import com.example.sales.orders.domain.model.aggregates.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByProfileId(Long profileId);

  @NonNull
  @EntityGraph(attributePaths = "details")
  List<Order> findAll();

  @NonNull
  @EntityGraph(attributePaths = "details")
  Optional<Order> findById(@org.jspecify.annotations.NonNull Long id);

  boolean existsByIdAndProfileId(Long id, Long profileId);

  boolean existsByProfileId(Long profileId);

  long countByProfileId(Long profileId);

  List<Order> findByOrderDateBetween(
    LocalDateTime startDate,
    LocalDateTime endDate
  );

  List<Order> findByTotalGreaterThanEqualOrderByTotalDesc(
    BigDecimal amount
  );

  void deleteByProfileId(Long profileId);
}
