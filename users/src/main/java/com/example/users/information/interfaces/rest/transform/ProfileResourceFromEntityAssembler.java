package com.example.users.information.interfaces.rest.transform;

import com.example.users.information.domain.model.aggregates.Profile;
import com.example.users.information.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {

  private ProfileResourceFromEntityAssembler() {
  }

  public static ProfileResource toResourceFromEntity(Profile profile){

    return new ProfileResource(
        profile.getId(),
        profile.getName(),
        profile.getEmail()
    );
  }
}
