package com.example.notifications.emails.domain.services;

import com.example.notifications.emails.domain.model.events.OrderCreatedEvent;

public interface EmailService {

  void sendWelcomeEmail(String to, String name);

  void sendOrderCreatedEmail(OrderCreatedEvent event);
}
