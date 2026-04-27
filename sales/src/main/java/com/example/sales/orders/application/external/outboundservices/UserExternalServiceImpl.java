package com.example.sales.orders.application.external.outboundservices;

import com.example.sales.orders.domain.services.UserExternalService;
import org.springframework.stereotype.Service;

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
}
