package com.example.users.information.application.integration.events;

import com.example.users.information.domain.model.commands.UpdateProfileImageInfoCommand;
import com.example.users.information.domain.model.events.ProfileImageUpdatedEvent;
import com.example.users.information.domain.services.ProfileCommandService;
import com.example.users.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MediaEventListener {

  private final ProfileCommandService profileCommandService;

  public MediaEventListener(
      ProfileCommandService profileCommandService
  ) {

    this.profileCommandService = profileCommandService;
  }

  @RabbitListener(queues = RabbitMQConfig.USER_IMAGE_UPDATED_QUEUE)
  public void handle(ProfileImageUpdatedEvent event){

    var command =
        new UpdateProfileImageInfoCommand(
            event.userId(),
            event.imageUrl(),
            event.publicId()
        );

    profileCommandService.handle(command);
  }
}
