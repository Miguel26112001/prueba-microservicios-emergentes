package com.example.notifications.emails.application.internal.services;

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
}
