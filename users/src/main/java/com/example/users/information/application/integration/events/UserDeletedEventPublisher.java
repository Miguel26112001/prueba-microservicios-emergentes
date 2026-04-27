package com.example.users.information.application.integration.events;

import com.example.users.information.domain.model.events.UserDeletedEvent;
import com.example.users.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserDeletedEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  public UserDeletedEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publish(Long userId) {
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE,
        RabbitMQConfig.ROUTING_KEY,
        new UserDeletedEvent(userId)
    );
  }
}
