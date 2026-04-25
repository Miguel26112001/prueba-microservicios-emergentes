package com.example.users.information.domain.services;

import java.util.List;
import java.util.Optional;
import com.example.users.information.domain.model.aggregates.User;
import com.example.users.information.domain.model.queries.GetAllUsersQuery;
import com.example.users.information.domain.model.queries.GetUserByEmailQuery;
import com.example.users.information.domain.model.queries.GetUserByIdQuery;

public interface UserQueryService {

  List<User> handle(GetAllUsersQuery query);

  Optional<User> handle(GetUserByIdQuery query);

  Optional<User> handle(GetUserByEmailQuery query);

}
