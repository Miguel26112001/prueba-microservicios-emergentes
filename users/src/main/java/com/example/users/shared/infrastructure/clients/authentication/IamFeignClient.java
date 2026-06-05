package com.example.users.shared.infrastructure.clients.authentication;

import com.example.users.shared.infrastructure.clients.authentication.resources.UserResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "iam-service")
public interface IamFeignClient {

  @GetMapping("/api/v1/users/{userId}")
  UserResource getUserById(
      @PathVariable Long userId
  );
}