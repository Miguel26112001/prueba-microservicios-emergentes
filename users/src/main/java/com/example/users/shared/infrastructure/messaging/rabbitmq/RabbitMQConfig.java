package com.example.users.shared.infrastructure.messaging.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE = "users.exchange";
  public static final String USER_CREATED_KEY = "user.created";
  public static final String USER_DELETED_KEY = "user.deleted";

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE);
  }

  @Bean
  public MessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
