package com.example.authentication.management.domain.services;

import com.example.authentication.management.domain.model.aggregates.User;
import com.example.authentication.management.domain.model.queries.GetUserByIdQuery;

public interface UserQueryService {

  User getUserById(GetUserByIdQuery query);
}