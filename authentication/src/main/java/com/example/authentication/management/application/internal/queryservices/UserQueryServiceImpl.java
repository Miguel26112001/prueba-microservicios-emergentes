package com.example.authentication.management.application.internal.queryservices;

import com.example.authentication.management.domain.model.aggregates.User;
import com.example.authentication.management.domain.model.queries.GetUserByIdQuery;
import com.example.authentication.management.domain.services.UserQueryService;
import com.example.authentication.management.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link UserQueryService} interface.
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {

  private final UserRepository userRepository;

  /**
   * Constructor.
   *
   * @param userRepository {@link UserRepository} instance.
   */
  public UserQueryServiceImpl(
      UserRepository userRepository
  ) {

    this.userRepository = userRepository;
  }

  /**
   * This method is used to handle {@link GetUserByIdQuery} query.
   * @param query {@link GetUserByIdQuery} instance.
   * @return {@link Optional} of {@link User} instance.
   * @see GetUserByIdQuery
   */
  @Override
  public Optional<User> getUserById(
      GetUserByIdQuery query
  ) {

    return userRepository.findUserById(query.userId());
  }
}