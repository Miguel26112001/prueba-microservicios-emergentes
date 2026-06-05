package com.example.users.information.interfaces.rest.transform;

import com.example.users.information.domain.model.commands.UpdateProfileCommand;
import com.example.users.information.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {

  private UpdateProfileCommandFromResourceAssembler() {
  }

  public static UpdateProfileCommand toCommandFromResource(Long userId, UpdateProfileResource resource){
    return new UpdateProfileCommand(
        userId,
        resource.name(),
        resource.email()
    );
  }
}
