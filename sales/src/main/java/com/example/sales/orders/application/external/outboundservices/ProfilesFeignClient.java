package com.example.sales.orders.application.external.outboundservices;

import com.example.sales.orders.application.external.resources.ProfileResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profiles-service")
public interface ProfilesFeignClient {

  @GetMapping("/api/v1/profiles/{id}")
  ProfileResource getProfileById(@PathVariable Long id);
}
