package com.example.sales.orders.application.external.outboundservices;

import com.example.sales.orders.application.external.resources.ProfileResource;
import com.example.sales.orders.domain.services.ProfileExternalService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilesExternalServiceImpl implements ProfileExternalService {

  private final ProfilesFeignClient profilesFeignClient;

  public ProfilesExternalServiceImpl(
      ProfilesFeignClient profilesFeignClient
  ) {

    this.profilesFeignClient = profilesFeignClient;
  }

  @Override
  public boolean existsProfile(
      Long profileId
  ) {

    try {
      return profilesFeignClient.getProfileById(profileId) != null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public Optional<ProfileResource> getProfileById(Long profileId) {

    try {

      var user = profilesFeignClient.getProfileById(profileId);
      return Optional.of(user);

    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
