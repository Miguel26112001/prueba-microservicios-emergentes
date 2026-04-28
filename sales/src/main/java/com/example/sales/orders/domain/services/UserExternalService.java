package com.example.sales.orders.domain.services;

import com.example.sales.orders.application.external.resources.UserResource;

import java.util.Optional;

public interface UserExternalService {
  boolean existsUser(Long userId);

  Optional<UserResource> getUserById(Long userId);

}
