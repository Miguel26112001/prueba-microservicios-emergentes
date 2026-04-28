package com.example.media.storage.domain.services;


import com.example.media.storage.application.external.resources.UserResource;

import java.util.Optional;

public interface UserExternalService {

  Optional<UserResource> getUserById(Long userId);
}
