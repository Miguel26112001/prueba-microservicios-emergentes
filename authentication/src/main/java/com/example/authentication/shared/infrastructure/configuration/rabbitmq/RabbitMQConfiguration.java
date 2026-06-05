package com.example.authentication.shared.infrastructure.configuration.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  // =========================
  // EXCHANGE
  // =========================
  public static final String IAM_EXCHANGE = "iam.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String USER_CREATED_KEY = "user.created";

  // =========================
  // EXCHANGE BEAN
  // =========================
  @Bean
  public TopicExchange iamExchange() {
    return new TopicExchange(IAM_EXCHANGE);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}