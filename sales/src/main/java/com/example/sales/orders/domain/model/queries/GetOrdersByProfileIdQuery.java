package com.example.sales.orders.domain.model.queries;

/**
 * Query to get orders by user id
 */
public record GetOrdersByProfileIdQuery(
  Long profileId
) {
}