package com.example.media.storage.domain.services;


import com.example.media.storage.application.external.resources.ProfilesResource;

import java.util.Optional;

public interface ProfilesExternalService {

  Optional<ProfilesResource> getProfileById(Long profileId);
}
