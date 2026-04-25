package com.example.sales.orders.application.internal.queryservices;

import com.example.sales.orders.domain.model.aggregates.Order;
import com.example.sales.orders.domain.model.queries.GetAllOrdersQuery;
import com.example.sales.orders.domain.model.queries.GetOrderByIdQuery;
import com.example.sales.orders.domain.model.queries.GetOrdersByUserIdQuery;
import com.example.sales.orders.domain.services.OrderQueryService;
import com.example.sales.orders.infrastructure.persistence.jpa.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

  private final OrderRepository orderRepository;

  public OrderQueryServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public List<Order> handle(GetAllOrdersQuery query) {
    return orderRepository.findAll();
  }

  @Override
  public Optional<Order> handle(GetOrderByIdQuery query) {
    return orderRepository.findById(query.orderId());
  }

  @Override
  public List<Order> handle(GetOrdersByUserIdQuery query) {
    return orderRepository.findByUserId(query.userId());
  }
}
