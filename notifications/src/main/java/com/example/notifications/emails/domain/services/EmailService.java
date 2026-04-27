package com.example.notifications.emails.domain.services;

public interface EmailService {

  void sendWelcomeEmail(String to, String name);
}
