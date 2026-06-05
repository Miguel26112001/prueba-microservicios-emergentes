package com.example.authentication.management.domain.services;

import com.example.authentication.management.domain.model.aggregates.User;
import com.example.authentication.management.domain.model.commands.SignInCommand;
import com.example.authentication.management.domain.model.commands.SignUpCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {

  Optional<ImmutablePair<User, String>> handle(SignInCommand command);

  Optional<User> handle(SignUpCommand command);
}