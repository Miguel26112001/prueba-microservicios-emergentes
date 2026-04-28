package com.example.media.storage.application.external.outboundservices;


import com.example.media.storage.application.external.resources.UserResource;
import com.example.media.storage.domain.services.UserExternalService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserExternalServiceImpl implements UserExternalService {

  private final UserFeignClient userFeignClient;

  public UserExternalServiceImpl(UserFeignClient userFeignClient) {
    this.userFeignClient = userFeignClient;
  }

  @Override
  public Optional<UserResource> getUserById(Long userId) {
    try {

      var user = userFeignClient.getUserById(userId);
      return Optional.of(user);

    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
