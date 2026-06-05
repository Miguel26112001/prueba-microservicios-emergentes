package com.example.users.information.application.integration.events;

import com.example.users.information.domain.model.commands.UpdateProfileImageInfoCommand;
import com.example.users.information.domain.model.events.ProfileImageUpdatedEvent;
import com.example.users.information.domain.services.ProfileCommandService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.example.users.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig.PROFILE_IMAGE_UPDATED_QUEUE;

@Service
public class MediaEventListener {

  private final ProfileCommandService profileCommandService;

  public MediaEventListener(
      ProfileCommandService profileCommandService
  ) {

    this.profileCommandService = profileCommandService;
  }

  @RabbitListener(queues = PROFILE_IMAGE_UPDATED_QUEUE)
  public void handle(ProfileImageUpdatedEvent event){

    var command =
        new UpdateProfileImageInfoCommand(
            event.profileId(),
            event.imageUrl(),
            event.publicId()
        );

    profileCommandService.handle(command);
  }
}
