package com.example.authentication.management.infrastructure.messaging;

import com.example.authentication.management.application.internal.outboundservices.messaging.IamEventPublisher;
import com.example.authentication.management.domain.model.events.UserCreatedEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.example.authentication.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.IAM_EXCHANGE;
import static com.example.authentication.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.USER_CREATED_KEY;

@Service
public class IamEventPublisherImpl implements IamEventPublisher {

  private final AmqpTemplate amqpTemplate;

  public IamEventPublisherImpl(
      AmqpTemplate amqpTemplate
  ) {

    this.amqpTemplate = amqpTemplate;
  }

  @Override
  public void publishUserCreated(
      UserCreatedEvent event
  ) {

    amqpTemplate.convertAndSend(
        IAM_EXCHANGE,
        USER_CREATED_KEY,
        event
    );
  }
}