package com.example.users.information.application.integration.events;

import com.example.users.information.domain.model.commands.UpdateUserImageInfoCommand;
import com.example.users.information.domain.model.events.UserImageUpdatedEvent;
import com.example.users.information.domain.services.UserCommandService;
import com.example.users.shared.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MediaEventListener {

  private final UserCommandService userCommandService;

  public MediaEventListener(UserCommandService userCommandService) {
    this.userCommandService = userCommandService;
  }

  @RabbitListener(queues = RabbitMQConfig.USER_IMAGE_UPDATED_QUEUE)
  public void handle(UserImageUpdatedEvent event){

    var command = new UpdateUserImageInfoCommand(event.userId(), event.imageUrl(), event.publicId());

    userCommandService.handle(command);
  }
}
