package com.example.users.information.domain.services;

import java.util.Optional;

import com.example.users.information.domain.model.aggregates.Profile;
import com.example.users.information.domain.model.commands.CreateProfileCommand;
import com.example.users.information.domain.model.commands.DeleteProfileCommand;
import com.example.users.information.domain.model.commands.UpdateProfileCommand;
import com.example.users.information.domain.model.commands.UpdateProfileImageInfoCommand;

public interface ProfileCommandService {

  Optional<Profile> handle(CreateProfileCommand command);

  Optional<Profile> handle(UpdateProfileCommand command);

  void handle(UpdateProfileImageInfoCommand command);

  void handle(DeleteProfileCommand command);
}