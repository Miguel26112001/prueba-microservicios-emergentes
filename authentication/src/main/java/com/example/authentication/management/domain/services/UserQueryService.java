package com.example.authentication.management.domain.services;

import com.example.authentication.management.domain.model.aggregates.User;
import com.example.authentication.management.domain.model.queries.GetUserByIdQuery;

import java.util.Optional;

public interface UserQueryService {

  Optional<User> getUserById(GetUserByIdQuery query);
}