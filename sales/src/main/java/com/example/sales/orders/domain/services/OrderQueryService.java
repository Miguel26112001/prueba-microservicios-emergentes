package com.example.sales.orders.domain.services;

import com.example.sales.orders.domain.model.aggregates.Order;
import com.example.sales.orders.domain.model.queries.GetAllOrdersQuery;
import com.example.sales.orders.domain.model.queries.GetOrderByIdQuery;
import com.example.sales.orders.domain.model.queries.GetOrdersByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface OrderQueryService {

  List<Order> handle(GetAllOrdersQuery query);

  Optional<Order> handle(GetOrderByIdQuery query);

  List<Order> handle(GetOrdersByUserIdQuery query);
}
