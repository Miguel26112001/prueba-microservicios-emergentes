package com.example.users.information.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import com.example.users.information.domain.model.aggregates.Profile;
import com.example.users.information.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

  Optional<Profile> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByIdNotAndEmail(Long id, String email);

  boolean existsByUserId_Value(Long userId);

  Optional<Profile> findByName(String name);
}