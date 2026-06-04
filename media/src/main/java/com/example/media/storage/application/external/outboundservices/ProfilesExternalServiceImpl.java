package com.example.media.storage.application.external.outboundservices;


import com.example.media.storage.application.external.resources.ProfilesResource;
import com.example.media.storage.domain.services.ProfilesExternalService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilesExternalServiceImpl implements ProfilesExternalService {

  private final ProfilesFeignClient profilesFeignClient;

  public ProfilesExternalServiceImpl(ProfilesFeignClient profilesFeignClient) {
    this.profilesFeignClient = profilesFeignClient;
  }

  @Override
  public Optional<ProfilesResource> getProfileById(Long profileId) {
    try {

      var profile = profilesFeignClient.getProfileById(profileId);
      return Optional.of(profile);

    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
