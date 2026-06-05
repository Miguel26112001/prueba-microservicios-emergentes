package com.example.sales.orders.domain.services;

import com.example.sales.orders.application.external.resources.ProfileResource;

import java.util.Optional;

public interface ProfileExternalService {

  boolean existsProfile(Long profileId);

  Optional<ProfileResource> getProfileById(Long profileId);
}