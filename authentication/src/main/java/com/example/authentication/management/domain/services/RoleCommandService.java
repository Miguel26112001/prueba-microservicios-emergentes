package com.example.authentication.management.domain.services;

import com.example.authentication.management.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {

  void handle(SeedRolesCommand command);
}