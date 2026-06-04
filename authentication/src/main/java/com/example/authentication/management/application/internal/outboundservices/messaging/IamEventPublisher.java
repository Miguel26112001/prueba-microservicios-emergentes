package com.example.authentication.management.application.internal.outboundservices.messaging;

import com.example.authentication.management.domain.model.events.UserCreatedEvent;

public interface IamEventPublisher {

  void publishUserCreated(UserCreatedEvent event);
}