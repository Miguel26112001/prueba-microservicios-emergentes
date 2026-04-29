package com.example.ai.agent.infrastructure.clients.users;

import com.example.ai.agent.domain.model.responses.UserResource;
import com.example.ai.agent.infrastructure.clients.users.requests.CreateUserRequest;
import com.example.ai.agent.infrastructure.clients.users.requests.UpdateUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "users-service")
public interface UsersClient {

  @GetMapping("/api/v1/users/{id}")
  UserResource getUserById(@PathVariable Long id);

  @GetMapping("/api/v1/users/email/{email}")
  UserResource getUserByEmail(@PathVariable String email);

  @GetMapping("/api/v1/users")
  List<UserResource> getAllUsers();

  @PostMapping("/api/v1/users")
  UserResource createUser(@RequestBody CreateUserRequest request);

  @PutMapping("/api/v1/users/{id}")
  UserResource updateUser(
      @PathVariable Long id,
      @RequestBody UpdateUserRequest request
  );

  @DeleteMapping("/api/v1/users/{id}")
  void deleteUser(@PathVariable Long id);
}
