package com.example.users.information.interfaces.rest.transform;

import com.example.users.information.domain.model.aggregates.User;
import com.example.users.information.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

  private UserResourceFromEntityAssembler() {
  }

  public static UserResource toResourceFromEntity(User user){

    return new UserResource(
        user.getId(),
        user.getName(),
        user.getEmail()
    );
  }
}
