package com.example.authentication.management.application.internal.commandservices;

import com.example.authentication.management.application.internal.outboundservices.hashing.HashingService;
import com.example.authentication.management.application.internal.outboundservices.tokens.TokenService;
import com.example.authentication.management.domain.exceptions.InvalidPasswordException;
import com.example.authentication.management.domain.exceptions.RoleNotFoundException;
import com.example.authentication.management.domain.exceptions.UserNotFoundException;
import com.example.authentication.management.domain.model.aggregates.User;
import com.example.authentication.management.domain.model.commands.SignInCommand;
import com.example.authentication.management.domain.model.commands.SignUpCommand;
import com.example.authentication.management.domain.services.UserCommandService;
import com.example.authentication.management.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.example.authentication.management.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.example.authentication.management.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User command service implementation
 * <p>
 *     This class implements the {@link UserCommandService} interface and provides the implementation for the
 *     {@link SignInCommand} and {@link SignUpCommand} commands.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final HashingService hashingService;
  private final TokenService tokenService;

  public UserCommandServiceImpl(
      RoleRepository roleRepository,
      UserRepository userRepository,
      HashingService hashingService,
      TokenService tokenService
  ) {

    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.hashingService = hashingService;
    this.tokenService = tokenService;
  }

  /**
   * Handle the sign-in command
   * <p>
   *     This method handles the {@link SignInCommand} command and returns the user and the token.
   * </p>
   * @param command the sign-in command containing the username and password
   * @return and optional containing the user matching the username and the generated token
   * @throws RuntimeException if the user is not found or the password is invalid
   */
  @Override
  public Optional<ImmutablePair<User, String>> handle(
      SignInCommand command
  ) {

    var user =userRepository.findByUsername(command.username())
        .orElseThrow(() -> UserNotFoundException.withUsername(command.username()));

    if (!hashingService.matches(command.password(), user.getPassword()))
      throw InvalidPasswordException.withMessage();

    UserDetailsImpl userDetails = UserDetailsImpl.build(user);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

    var token = tokenService.generateToken(authentication);

    return Optional.of(ImmutablePair.of(user, token));
  }

  /**
   * Handle the sign-up command
   * <p>
   *     This method handles the {@link SignUpCommand} command and returns the user.
   * </p>
   * @param command the sign-up command containing the username and password
   * @return the created user
   */
  @Override
  public Optional<User> handle(
      SignUpCommand command
  ) {

    if (userRepository.existsByUsername(command.username())) {
      throw UserNotFoundException.withUsername(command.username());
    }

    var roles = command.roles().stream()
        .map(role ->
            roleRepository.findByName(role.getName())
                .orElseThrow(() -> RoleNotFoundException.withName(role.getStringName())))
        .toList();

    var user = new User(
        command.username(),
        hashingService.encode(command.password()),
        roles
    );

    var savedUser = userRepository.save(user);

    return Optional.of(savedUser);
  }
}