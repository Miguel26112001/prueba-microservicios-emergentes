package com.example.authentication.management.domain.services;

import com.example.authentication.management.domain.model.entities.Role;
import com.example.authentication.management.domain.model.queries.GetAllRolesQuery;

import java.util.List;

public interface RoleQueryService {

  List<Role> handle(GetAllRolesQuery query);
}