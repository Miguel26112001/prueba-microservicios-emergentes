package com.example.sales.orders.infrastructure.persistence.jpa.repositories;

import com.example.sales.orders.domain.model.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

  List<OrderDetail> findByOrderId(Long orderId);

  List<OrderDetail> findByProductId(Long productId);

  boolean existsByOrderIdAndProductId(Long orderId, Long productId);

  long countByOrderId(Long orderId);

  void deleteByOrderId(Long orderId);
}
