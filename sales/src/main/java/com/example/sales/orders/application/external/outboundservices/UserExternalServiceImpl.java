package com.example.sales.orders.application.external.outboundservices;

import com.example.sales.orders.application.external.resources.UserResource;
import com.example.sales.orders.domain.services.UserExternalService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserExternalServiceImpl implements UserExternalService {

  private final UserFeignClient userFeignClient;

  public UserExternalServiceImpl(UserFeignClient userFeignClient) {
    this.userFeignClient = userFeignClient;
  }

  @Override
  public boolean existsUser(Long userId) {
    try {
      return userFeignClient.getUserById(userId) != null;
    } catch (Exception e) {
      return false;
    }
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
