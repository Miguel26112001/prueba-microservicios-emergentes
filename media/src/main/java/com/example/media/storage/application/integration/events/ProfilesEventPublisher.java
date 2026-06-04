package com.example.media.storage.application.integration.events;

import com.example.media.storage.domain.model.events.ProfileImageUpdatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.example.media.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig.PROFILES_EXCHANGE;
import static com.example.media.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig.PROFILE_IMAGE_UPDATED_KEY;

@Service
public class ProfilesEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  public ProfilesEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishProfileImageUpdated(ProfileImageUpdatedEvent event) {

    rabbitTemplate.convertAndSend(
        PROFILES_EXCHANGE,
        PROFILE_IMAGE_UPDATED_KEY,
        event
    );
  }
}
