package com.example.media.storage.application.external.outboundservices;

import com.example.media.storage.application.external.resources.UserResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service")
public interface UserFeignClient {

  @GetMapping("/api/v1/users/{id}")
  UserResource getUserById(@PathVariable Long id);
}
