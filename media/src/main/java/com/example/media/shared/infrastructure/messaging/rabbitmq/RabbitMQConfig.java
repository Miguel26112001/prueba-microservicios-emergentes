package com .example.media.shared.infrastructure.messaging.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String USERS_EXCHANGE = "users.exchange";
  public static final String USER_IMAGE_UPDATED_KEY = "user.image.updated";

  @Bean
  public TopicExchange usersExchange() {
    return new TopicExchange(USERS_EXCHANGE);
  }

  @Bean
  public MessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
