package com.example.authentication.management.infrastructure.persistence.jpa.repositories;

import com.example.authentication.management.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * This method is responsible for checking if the user exists by username.
   * @param username The username.
   * @return True if the user exists, false otherwise.
   */
  boolean existsByUsername(String username);

  @EntityGraph(attributePaths = {"roles"})
  Optional<User> findByUsername(String username);

  @EntityGraph(attributePaths = {"roles"})
  Optional<User> findUserById(Long id);
}