package com.example.sales.orders.application.integration.messaging;

import com.example.sales.orders.domain.model.events.OrderCreatedEvent;
import com.example.sales.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishOrderCreated(OrderCreatedEvent event) {
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.ORDERS_EXCHANGE,
        RabbitMQConfig.ORDER_CREATED_KEY,
        event
    );
  }
}
