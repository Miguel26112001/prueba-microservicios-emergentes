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

  // =========================
  // EXCHANGES
  // =========================
  public static final String IAM_EXCHANGE = "iam.exchange";
  public static final String PROFILES_EXCHANGE = "profiles.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String USER_CREATED_KEY = "user.created";
  public static final String PROFILE_CREATED_KEY = "profile.created";
  public static final String PROFILE_DELETED_KEY = "profile.deleted";
  public static final String PROFILE_IMAGE_UPDATED_KEY = "profile.image.updated";

  // =========================
  // QUEUES
  // =========================
  public static final String USER_CREATED_QUEUE = "profiles.user.created.queue";
  public static final String PROFILE_IMAGE_UPDATED_QUEUE = "media.profile.image.updated.queue";

  // =========================
  // EXCHANGE BEAN
  // =========================
  @Bean
  public TopicExchange iamExchange() {
    return new TopicExchange(IAM_EXCHANGE);
  }

  @Bean
  public TopicExchange profilesExchange() {
    return new TopicExchange(PROFILES_EXCHANGE);
  }

  // =========================
  // QUEUE
  // =========================
  @Bean
  public Queue userCreatedQueue() {
    return new Queue(USER_CREATED_QUEUE);
  }

  @Bean
  public Queue profileImageUpdatedQueue() {
    return new Queue(PROFILE_IMAGE_UPDATED_QUEUE);
  }

  // =========================
  // BINDING
  // =========================
  @Bean
  public Binding userCreatedBinding() {

    return BindingBuilder
        .bind(userCreatedQueue())
        .to(iamExchange())
        .with(USER_CREATED_KEY);
  }

  @Bean
  public Binding profileImageUpdatedBinding() {

    return BindingBuilder
        .bind(profileImageUpdatedQueue())
        .to(profilesExchange())
        .with(PROFILE_IMAGE_UPDATED_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
