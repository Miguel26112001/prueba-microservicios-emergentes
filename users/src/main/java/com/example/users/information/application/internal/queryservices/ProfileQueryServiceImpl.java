package com.example.users.information.application.internal.queryservices;

import java.util.List;
import java.util.Optional;

import com.example.users.information.domain.model.aggregates.Profile;
import com.example.users.information.domain.model.queries.*;
import org.springframework.stereotype.Service;
import com.example.users.information.domain.exceptions.ProfileWithEmailNotFoundException;
import com.example.users.information.domain.exceptions.ProfileWithIdNotFoundException;
import com.example.users.information.domain.services.ProfileQueryService;
import com.example.users.information.infrastructure.persistence.jpa.repositories.ProfileRepository;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

  private final ProfileRepository profileRepository;

  public ProfileQueryServiceImpl(
      ProfileRepository profileRepository
  ) {

    this.profileRepository = profileRepository;
  }

  @Override
  public List<Profile> handle(
      GetAllProfileQuery query
  ) {

    return profileRepository.findAll();
  }

  @Override
  public Optional<Profile> handle(
      GetProfileByIdQuery query
  ) {

    var userOptional = profileRepository.findById(query.userId());

    if (userOptional.isEmpty()) {
      throw new ProfileWithIdNotFoundException(query.userId());
    }

    return userOptional;
  }

  @Override
  public Optional<Profile> handle(
      GetProfileByEmailQuery query
  ) {

    var userOptional = profileRepository.findByEmail(query.email());

    if (userOptional.isEmpty()) {
      throw new ProfileWithEmailNotFoundException(query.email());
    }

    return profileRepository
        .findByEmail(query.email());
  }

  @Override
  public Optional<Profile> handle(
      GetProfileByNameQuery query
  ) {

    return profileRepository
        .findByName(query.name());
  }

  @Override
  public Optional<Profile> handle(
      GetMyProfileQuery query
  ) {

    return profileRepository
        .findByUserId_Value(query.userId());
  }
}
