package com.example.users.information.interfaces.rest.transform;

import com.example.users.information.domain.model.commands.CreateUserCommand;
import com.example.users.information.interfaces.rest.resources.CreateUserResource;

public class CreateUserCommandFromResourceAssembler {

  private CreateUserCommandFromResourceAssembler() {
  }

  public static CreateUserCommand toCommandFromResource(CreateUserResource resource){

    return new CreateUserCommand(
        resource.name(),
        resource.email()
    );}
}
