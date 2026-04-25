package com.example.users.information.domain.services;

import java.util.Optional;
import com.example.users.information.domain.model.aggregates.User;
import com.example.users.information.domain.model.commands.CreateUserCommand;
import com.example.users.information.domain.model.commands.DeleteUserCommand;
import com.example.users.information.domain.model.commands.UpdateUserCommand;

public interface UserCommandService {

  Optional<User> handle(CreateUserCommand command);

  Optional<User> handle(UpdateUserCommand command);

  void handle(DeleteUserCommand command);

}
