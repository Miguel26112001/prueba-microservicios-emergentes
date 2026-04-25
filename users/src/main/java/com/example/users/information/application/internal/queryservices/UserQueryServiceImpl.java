package com.example.users.information.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.users.information.domain.exceptions.UserWithEmailNotFoundException;
import com.example.users.information.domain.exceptions.UserWithIdNotFoundException;
import com.example.users.information.domain.model.aggregates.User;
import com.example.users.information.domain.model.queries.GetAllUsersQuery;
import com.example.users.information.domain.model.queries.GetUserByEmailQuery;
import com.example.users.information.domain.model.queries.GetUserByIdQuery;
import com.example.users.information.domain.services.UserQueryService;
import com.example.users.information.infrastructure.persistence.jpa.repositories.UserRepository;

@Service
public class UserQueryServiceImpl implements UserQueryService {

  private final UserRepository userRepository;

  public UserQueryServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> handle(GetAllUsersQuery query) {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> handle(GetUserByIdQuery query) {

    var userOptional = userRepository.findById(query.userId());
    if (userOptional.isEmpty()) {
      throw new UserWithIdNotFoundException(query.userId());
    }

    return userOptional;
  }

  @Override
  public Optional<User> handle(GetUserByEmailQuery query) {

    var userOptional = userRepository.findByEmail(query.email());
    if (userOptional.isEmpty()) {
      throw new UserWithEmailNotFoundException(query.email());
    }

    return userRepository.findByEmail(query.email());
  }
}
