package com.example.users.information.application.integration.events;

import com.example.users.information.domain.model.events.UserCreatedEvent;
import com.example.users.information.domain.model.events.UserDeletedEvent;
import com.example.users.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  public UserEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishUserCreated(UserCreatedEvent event) {
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE,
        RabbitMQConfig.USER_CREATED_KEY,
        event
    );
  }

  public void publish(Long userId) {
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE,
        RabbitMQConfig.USER_DELETED_KEY,
        new UserDeletedEvent(userId)
    );
  }
}