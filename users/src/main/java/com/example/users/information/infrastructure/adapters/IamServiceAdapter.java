package com.example.users.information.infrastructure.adapters;

import com.example.users.information.application.internal.outboundservices.ports.IamQueryPort;
import com.example.users.shared.infrastructure.clients.authentication.IamFeignClient;
import com.example.users.shared.infrastructure.clients.authentication.resources.UserResource;
import org.springframework.stereotype.Component;

@Component
public class IamServiceAdapter implements IamQueryPort {

  private final IamFeignClient iamFeignClient;

  public IamServiceAdapter(
      IamFeignClient iamFeignClient
  ) {

    this.iamFeignClient = iamFeignClient;
  }

  @Override
  public UserResource getUserById(
      Long userId
  ) {

    return iamFeignClient.getUserById(userId);
  }
}