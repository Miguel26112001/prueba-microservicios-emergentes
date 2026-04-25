package com.example.users.information.interfaces.rest.transform;

import com.example.users.information.domain.model.commands.UpdateUserCommand;
import com.example.users.information.interfaces.rest.resources.UpdateUserResource;

public class UpdateUserCommandFromResourceAssembler {

  private UpdateUserCommandFromResourceAssembler() {
  }

  public static UpdateUserCommand toCommandFromResource(Long userId, UpdateUserResource resource){
    return new UpdateUserCommand(
        userId,
        resource.name(),
        resource.email()
    );
  }
}
