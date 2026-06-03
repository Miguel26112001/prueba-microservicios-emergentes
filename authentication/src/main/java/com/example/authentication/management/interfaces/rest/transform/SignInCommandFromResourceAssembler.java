package com.example.authentication.management.interfaces.rest.transform;

import com.example.authentication.management.domain.model.commands.SignInCommand;
import com.example.authentication.management.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {

  private SignInCommandFromResourceAssembler() {
  }

  public static SignInCommand toCommandFromResource(
      SignInResource resource
  ) {

    return new SignInCommand(
        resource.username(),
        resource.password()
    );
  }
}