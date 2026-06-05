package com.example.users.information.application.internal.outboundservices.ports;

import com.example.users.shared.infrastructure.clients.authentication.resources.UserResource;

public interface IamQueryPort {

  UserResource getUserById(Long userId);
}