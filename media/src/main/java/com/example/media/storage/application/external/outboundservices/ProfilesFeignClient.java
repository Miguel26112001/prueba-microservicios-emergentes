package com.example.media.storage.application.external.outboundservices;

import com.example.media.storage.application.external.resources.ProfilesResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profiles-service")
public interface ProfilesFeignClient {

  @GetMapping("/api/v1/profiles/{id}")
  ProfilesResource getProfileById(@PathVariable Long id);
}
