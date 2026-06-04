package com.example.ai.agent.infrastructure.clients.users;

import com.example.ai.agent.domain.model.responses.ProfilesResource;
import com.example.ai.agent.infrastructure.clients.users.requests.CreateProfileRequest;
import com.example.ai.agent.infrastructure.clients.users.requests.UpdateProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "profiles-service")
public interface ProfilesClient {

  @GetMapping("/api/v1/profiles/{profileId}")
  ProfilesResource getProfileById(@PathVariable Long profileId);

  @GetMapping("/api/v1/profiles/email/{email}")
  ProfilesResource getProfileByEmail(@PathVariable String email);

  @GetMapping("/api/v1/profiles")
  List<ProfilesResource> getAllProfiles();

  @PostMapping("/api/v1/profiles")
  ProfilesResource createProfile(@RequestBody CreateProfileRequest request);

  @PutMapping("/api/v1/profiles/{profileId}")
  ProfilesResource updateProfile(
      @PathVariable Long profileId,
      @RequestBody UpdateProfileRequest request
  );

  @DeleteMapping("/api/v1/profiles/{profileId}")
  void deleteProfile(@PathVariable Long profileId);
}
