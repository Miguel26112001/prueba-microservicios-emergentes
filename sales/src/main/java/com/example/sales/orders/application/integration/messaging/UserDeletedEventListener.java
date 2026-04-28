package com.example.sales.orders.application.integration.messaging;

import com.example.sales.orders.domain.model.events.UserDeletedEvent;
import com.example.sales.orders.infrastructure.persistence.jpa.repositories.OrderRepository;
import com.example.sales.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDeletedEventListener {

  private final OrderRepository orderRepository;

  public UserDeletedEventListener(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE)
  @Transactional
  public void handle(UserDeletedEvent event) {

    var orders = orderRepository.findByUserId(event.userId());

    if (orders.isEmpty()) {
      return;
    }

    orderRepository.deleteAll(orders);
    System.out.println("Orders deleted for user: " + event.userId());
  }
}
