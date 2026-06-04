package com.example.users.information.application.integration.events;

import com.example.users.information.domain.model.commands.CreateProfileFromEventCommand;
import com.example.users.information.domain.model.events.UserCreatedEvent;
import com.example.users.information.domain.services.ProfileCommandService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.example.users.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig.USER_CREATED_QUEUE;

@Component
public class IamEventListener {

  private final ProfileCommandService profileCommandService;

  public IamEventListener(
      ProfileCommandService profileCommandService
  ) {

    this.profileCommandService = profileCommandService;
  }

  @RabbitListener(queues = USER_CREATED_QUEUE)
  public void handle(
      UserCreatedEvent event
  ) {

    var command = new CreateProfileFromEventCommand(
        event.userId(),
        event.name(),
        event.email()
    );

    profileCommandService.handle(command);
  }
}