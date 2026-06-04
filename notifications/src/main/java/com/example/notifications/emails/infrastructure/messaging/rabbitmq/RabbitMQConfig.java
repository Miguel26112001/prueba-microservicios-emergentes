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

  // =========================
  // EXCHANGES
  // =========================
  public static final String PROFILES_EXCHANGE = "profiles.exchange";
  public static final String ORDERS_EXCHANGE = "orders.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String PROFILE_CREATED_KEY = "profile.created";
  public static final String ORDER_CREATED_KEY = "order.created";

  // =========================
  // QUEUES
  // =========================
  public static final String PROFILE_CREATED_QUEUE = "notification.profiles.created.queue";
  public static final String ORDER_CREATED_QUEUE = "notification.order.created.queue";

  // =========================
  // EXCHANGE BEAN
  // =========================
  @Bean
  public TopicExchange profilesExchange() {
    return new TopicExchange(PROFILES_EXCHANGE);
  }

  @Bean
  public TopicExchange ordersExchange() {
    return new TopicExchange(ORDERS_EXCHANGE);
  }

  // =========================
  // QUEUE
  // =========================
  @Bean
  public Queue profileCreatedQueue() {
    return new Queue(PROFILE_CREATED_QUEUE, true);
  }

  @Bean
  public Queue orderCreatedQueue() {
    return new Queue(ORDER_CREATED_QUEUE, true);
  }

  // =========================
  // BINDING
  // =========================
  @Bean
  public Binding profileCreatedBinding() {
    return BindingBuilder
        .bind(profileCreatedQueue())
        .to(profilesExchange())
        .with(PROFILE_CREATED_KEY);
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