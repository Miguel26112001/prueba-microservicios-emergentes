package com.example.notifications.emails.application.integration.messaging;

import com.example.notifications.emails.domain.model.events.OrderCreatedEvent;
import com.example.notifications.emails.domain.services.EmailService;
import com.example.notifications.emails.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

  private final EmailService emailService;

  public OrderEventListener(EmailService emailService) {
    this.emailService = emailService;
  }

  @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
  public void handle(OrderCreatedEvent event) {
    System.out.println("Evento recibido: " + event);

    emailService.sendOrderCreatedEmail(event);
  }
}
