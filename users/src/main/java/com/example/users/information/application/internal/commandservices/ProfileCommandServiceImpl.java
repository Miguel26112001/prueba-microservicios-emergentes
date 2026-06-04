package com.example.users.information.application.internal.commandservices;

import java.util.Optional;

import com.example.users.information.application.integration.events.ProfileEventPublisher;
import com.example.users.information.application.internal.outboundservices.ports.IamQueryPort;
import com.example.users.information.domain.exceptions.ProfileAlreadyExistsException;
import com.example.users.information.domain.model.aggregates.Profile;
import com.example.users.information.domain.model.commands.UpdateProfileImageInfoCommand;
import com.example.users.information.domain.model.events.ProfileCreatedEvent;
import org.springframework.stereotype.Service;
import com.example.users.information.domain.exceptions.EmailAlreadyExistsException;
import com.example.users.information.domain.exceptions.ProfileWithIdNotFoundException;
import com.example.users.information.domain.model.commands.CreateProfileCommand;
import com.example.users.information.domain.model.commands.DeleteProfileCommand;
import com.example.users.information.domain.model.commands.UpdateProfileCommand;
import com.example.users.information.domain.services.ProfileCommandService;
import com.example.users.information.infrastructure.persistence.jpa.repositories.ProfileRepository;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

  private final ProfileRepository profileRepository;
  private final IamQueryPort iamQueryPort;
  private final ProfileEventPublisher publisher;

  public ProfileCommandServiceImpl(
      ProfileRepository profileRepository,
      IamQueryPort iamQueryPort,
      ProfileEventPublisher publisher
  ) {

    this.profileRepository = profileRepository;
    this.iamQueryPort = iamQueryPort;
    this.publisher = publisher;
  }

  @Override
  public Optional<Profile> handle(
      CreateProfileCommand command
  ) {

    if (profileRepository.existsByEmail(command.email())) {
      throw new EmailAlreadyExistsException(command.email());
    }

    if (profileRepository.existsByUserId_Value(command.userId())) {
      throw ProfileAlreadyExistsException.withUserId(command.userId());
    }

    var user = iamQueryPort.getUserById(command.userId());

    if (user == null) {
      throw new RuntimeException("User not found");
    }

    var newUser = new Profile(command);

    profileRepository.save(newUser);

    publisher.publishUserCreated(
        new ProfileCreatedEvent(
            newUser.getName(),
            newUser.getEmail()
        )
    );

    return profileRepository.findByEmail(command.email());
  }

  @Override
  public Optional<Profile> handle(
      UpdateProfileCommand command
  ) {

    var user = profileRepository.findById(command.userId());

    if (user.isEmpty()) {
      throw new ProfileWithIdNotFoundException(command.userId());
    }

    if (profileRepository.existsByIdNotAndEmail(command.userId(), command.email())) {
      throw new EmailAlreadyExistsException(command.email());
    }

    var userToUpdate = user.get();

    userToUpdate.update(command);

    profileRepository.save(userToUpdate);

    return Optional.of(userToUpdate);
  }

  @Override
  public void handle(
      UpdateProfileImageInfoCommand command
  ) {

    var userOptional = profileRepository.findById(command.userId());

    if (userOptional.isEmpty()) {
      throw new ProfileWithIdNotFoundException(command.userId());
    }

    var user = userOptional.get();

    user.updateImageInfo(command.imageUrl(), command.publicId());

    profileRepository.save(user);
  }

  @Override
  public void handle(
      DeleteProfileCommand command
  ) {

    var user = profileRepository.findById(command.userId());

    if (user.isEmpty()) {
      throw new ProfileWithIdNotFoundException(command.userId());
    }

    profileRepository.delete(user.get());

    publisher.publish(command.userId());
  }
}
