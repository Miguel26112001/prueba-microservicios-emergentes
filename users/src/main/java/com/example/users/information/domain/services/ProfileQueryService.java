package com.example.users.information.domain.services;

import java.util.List;
import java.util.Optional;

import com.example.users.information.domain.model.aggregates.Profile;
import com.example.users.information.domain.model.queries.*;

public interface ProfileQueryService {

  List<Profile> handle(GetAllProfileQuery query);

  Optional<Profile> handle(GetProfileByIdQuery query);

  Optional<Profile> handle(GetProfileByEmailQuery query);

  Optional<Profile> handle(GetProfileByNameQuery query);

  Optional<Profile> handle(GetMyProfileQuery query);
}