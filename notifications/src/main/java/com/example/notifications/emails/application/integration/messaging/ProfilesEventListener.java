package com.example.notifications.emails.application.integration.messaging;

import com.example.notifications.emails.domain.model.events.ProfileCreatedEvent;
import com.example.notifications.emails.domain.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.example.notifications.emails.infrastructure.messaging.rabbitmq.RabbitMQConfig.PROFILE_CREATED_QUEUE;

@Component
public class ProfilesEventListener {

  private final EmailService emailService;

  public ProfilesEventListener(EmailService emailService) {
    this.emailService = emailService;
  }

  @RabbitListener(queues = PROFILE_CREATED_QUEUE)
  public void handle(ProfileCreatedEvent event) {

    emailService.sendWelcomeEmail(
        event.email(),
        event.name()
    );
  }
}
