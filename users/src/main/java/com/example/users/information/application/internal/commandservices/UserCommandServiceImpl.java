package com.example.users.information.application.internal.commandservices;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.users.information.domain.exceptions.EmailAlreadyExistsException;
import com.example.users.information.domain.exceptions.UserWithIdNotFoundException;
import com.example.users.information.domain.model.aggregates.User;
import com.example.users.information.domain.model.commands.CreateUserCommand;
import com.example.users.information.domain.model.commands.DeleteUserCommand;
import com.example.users.information.domain.model.commands.UpdateUserCommand;
import com.example.users.information.domain.services.UserCommandService;
import com.example.users.information.infrastructure.persistence.jpa.repositories.UserRepository;

@Service
public class UserCommandServiceImpl implements UserCommandService {

  private final UserRepository userRepository;

  public UserCommandServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> handle(CreateUserCommand command) {

    if (userRepository.existsByEmail(command.email())) {
      throw new EmailAlreadyExistsException(command.email());
    }

    var newUser = new User(command);
    userRepository.save(newUser);

    return userRepository.findByEmail(command.email());
  }

  @Override
  public Optional<User> handle(UpdateUserCommand command) {

    var user = userRepository.findById(command.userId());
    if (user.isEmpty()) {
      throw new UserWithIdNotFoundException(command.userId());
    }

    if (userRepository.existsByIdNotAndEmail(command.userId(), command.email())) {
      throw new EmailAlreadyExistsException(command.email());
    }

    var userToUpdate = user.get();
    userToUpdate.update(command);
    userRepository.save(userToUpdate);

    return Optional.of(userToUpdate);
  }

  @Override
  public void handle(DeleteUserCommand command) {
    var user = userRepository.findById(command.userId());
    if (user.isEmpty()) {
      throw new UserWithIdNotFoundException(command.userId());
    }

    userRepository.delete(user.get());
  }

}
