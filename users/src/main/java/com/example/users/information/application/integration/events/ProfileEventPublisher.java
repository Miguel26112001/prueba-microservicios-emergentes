package com.example.users.information.application.integration.events;

import com.example.users.information.domain.model.events.ProfileCreatedEvent;
import com.example.users.information.domain.model.events.ProfileDeletedEvent;
import com.example.users.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProfileEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  public ProfileEventPublisher(
      RabbitTemplate rabbitTemplate
  ) {

    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishProfileCreated(
      ProfileCreatedEvent event
  ) {

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.PROFILES_EXCHANGE,
        RabbitMQConfig.PROFILE_CREATED_KEY,
        event
    );
  }

  public void publish(
      Long profileId
  ) {

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.PROFILES_EXCHANGE,
        RabbitMQConfig.PROFILE_DELETED_KEY,
        new ProfileDeletedEvent(profileId)
    );
  }
}