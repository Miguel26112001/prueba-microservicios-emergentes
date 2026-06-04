package com.example.users.information.domain.services;

import java.util.Optional;

import com.example.users.information.domain.model.aggregates.Profile;
import com.example.users.information.domain.model.commands.*;

public interface ProfileCommandService {

  Optional<Profile> handle(CreateProfileCommand command);

  void handle(CreateProfileFromEventCommand command);

  Optional<Profile> handle(UpdateProfileCommand command);

  void handle(UpdateProfileImageInfoCommand command);

  void handle(DeleteProfileCommand command);
}