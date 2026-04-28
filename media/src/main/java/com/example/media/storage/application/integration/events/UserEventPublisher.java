package com.example.media.storage.application.integration.events;

import com.example.media.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import com.example.media.storage.domain.model.events.UserImageUpdatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  public UserEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishUserImageUpdated(UserImageUpdatedEvent event) {
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.USERS_EXCHANGE,
        RabbitMQConfig.USER_IMAGE_UPDATED_KEY,
        event
    );
  }
}
