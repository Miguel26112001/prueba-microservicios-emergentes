package com.example.users.shared.infrastructure.messaging.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE = "users.exchange";
  public static final String USER_CREATED_KEY = "user.created";
  public static final String USER_DELETED_KEY = "user.deleted";
  public static final String USER_IMAGE_UPDATED_KEY = "user.image.updated";
  public static final String USER_IMAGE_UPDATED_QUEUE = "media.user.image.updated.queue";

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE);
  }

  @Bean
  public Queue mediaQueue() {
    return new Queue(USER_IMAGE_UPDATED_QUEUE);
  }

  @Bean
  public Binding mediaBinding() {
    return BindingBuilder
        .bind(mediaQueue())
        .to(exchange())
        .with(USER_IMAGE_UPDATED_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
