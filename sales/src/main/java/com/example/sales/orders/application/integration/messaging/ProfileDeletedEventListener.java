package com.example.sales.orders.application.integration.messaging;

import com.example.sales.orders.domain.model.events.ProfileDeletedEvent;
import com.example.sales.orders.infrastructure.persistence.jpa.repositories.OrderRepository;
import com.example.sales.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileDeletedEventListener {

  private final OrderRepository orderRepository;

  public ProfileDeletedEventListener(
      OrderRepository orderRepository
  ) {

    this.orderRepository = orderRepository;
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE)
  @Transactional
  public void handle(ProfileDeletedEvent event) {

    var orders = orderRepository.findByProfileId(event.userId());

    if (orders.isEmpty()) {
      return;
    }

    orderRepository.deleteAll(orders);
    System.out.println("Orders deleted for user: " + event.userId());
  }
}
