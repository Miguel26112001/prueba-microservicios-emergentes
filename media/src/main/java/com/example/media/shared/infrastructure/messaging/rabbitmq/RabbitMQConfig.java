package com .example.media.shared.infrastructure.messaging.rabbitmq;

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

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String PROFILE_IMAGE_UPDATED_KEY = "profile.image.updated";

  // =========================
  // EXCHANGE BEAN
  // =========================
  @Bean
  public TopicExchange profilesExchange() {
    return new TopicExchange(PROFILES_EXCHANGE);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
