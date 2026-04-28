package com.example.notifications.emails.infrastructure.messaging.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  // ==========================================
  // USERS EVENTS
  // ==========================================
  public static final String USERS_EXCHANGE = "users.exchange";
  public static final String USER_CREATED_KEY = "user.created";
  public static final String USER_CREATED_QUEUE =
      "notification.user.created.queue";

  // ==========================================
  // ORDERS EVENTS
  // ==========================================
  public static final String ORDERS_EXCHANGE = "orders.exchange";
  public static final String ORDER_CREATED_KEY = "order.created";
  public static final String ORDER_CREATED_QUEUE =
      "notification.order.created.queue";

  // ==========================================
  // USERS CONFIG
  // ==========================================
  @Bean
  public TopicExchange usersExchange() {
    return new TopicExchange(USERS_EXCHANGE);
  }

  @Bean
  public Queue userCreatedQueue() {
    return new Queue(USER_CREATED_QUEUE, true);
  }

  @Bean
  public Binding userCreatedBinding() {
    return BindingBuilder
        .bind(userCreatedQueue())
        .to(usersExchange())
        .with(USER_CREATED_KEY);
  }

  // ==========================================
  // ORDERS CONFIG
  // ==========================================
  @Bean
  public TopicExchange ordersExchange() {
    return new TopicExchange(ORDERS_EXCHANGE);
  }

  @Bean
  public Queue orderCreatedQueue() {
    return new Queue(ORDER_CREATED_QUEUE, true);
  }

  @Bean
  public Binding orderCreatedBinding() {
    return BindingBuilder
        .bind(orderCreatedQueue())
        .to(ordersExchange())
        .with(ORDER_CREATED_KEY);
  }

  // ==========================================
  // JSON CONVERTER
  // ==========================================
  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
