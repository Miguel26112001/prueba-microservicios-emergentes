package com.example.notifications.emails.application.internal.services;

import com.example.notifications.emails.domain.model.events.OrderCreatedEvent;
import com.example.notifications.emails.domain.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
public class SendGridEmailService implements EmailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  public SendGridEmailService(
      JavaMailSender mailSender,
      TemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  @Value("${app.mail.from}")
  private String from;

  @Override
  public void sendWelcomeEmail(String to, String name) {
    try {
      MimeMessage message = mailSender.createMimeMessage();

      MimeMessageHelper helper =
          new MimeMessageHelper(
              message,
              MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name()
          );

      Context context = new Context();
      context.setVariable("name", name);

      String html =
          templateEngine.process(
              "welcome-email",
              context
          );

      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject("Bienvenido a nuestra plataforma");
      helper.setText(html, true);

      mailSender.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException("Error sending email", e);
    }
  }

  @Override
  public void sendOrderCreatedEmail(OrderCreatedEvent event) {
    try {
      MimeMessage message = mailSender.createMimeMessage();

      MimeMessageHelper helper =
          new MimeMessageHelper(
              message,
              MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name()
          );

      Context context = new Context();
      context.setVariable("name", event.name());
      context.setVariable("orderDate", event.orderDate());
      context.setVariable("total", event.total());
      context.setVariable("items", event.items());

      String html =
          templateEngine.process(
              "order-created-email",
              context
          );

      helper.setFrom(from);
      helper.setTo(event.email());
      helper.setSubject("Confirmación de compra");
      helper.setText(html, true);

      mailSender.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException("Error sending order email", e);
    }
  }
}
