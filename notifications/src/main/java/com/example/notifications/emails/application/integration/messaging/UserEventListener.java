package com.example.notifications.emails.application.integration.messaging;

import com.example.notifications.emails.domain.model.events.UserCreatedEvent;
import com.example.notifications.emails.domain.services.EmailService;
import com.example.notifications.emails.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

  private final EmailService emailService;

  public UserEventListener(EmailService emailService) {
    this.emailService = emailService;
  }

  @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
  public void handle(UserCreatedEvent event) {

    emailService.sendWelcomeEmail(
        event.email(),
        event.name()
    );
  }
}
