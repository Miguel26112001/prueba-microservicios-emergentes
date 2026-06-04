package com.example.users.information.interfaces.rest.transform;

import com.example.users.information.domain.model.commands.CreateProfileCommand;
import com.example.users.information.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {

  private CreateProfileCommandFromResourceAssembler() {
  }

  public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource){

    return new CreateProfileCommand(
        resource.userId(),
        resource.name(),
        resource.email()
    );}
}
