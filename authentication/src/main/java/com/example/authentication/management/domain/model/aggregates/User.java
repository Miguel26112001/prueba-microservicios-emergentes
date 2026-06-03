package com.example.authentication.management.domain.model.aggregates;

import com.example.authentication.management.domain.model.entities.Role;
import com.example.authentication.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(nullable = false, length = 120)
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  public User() {
    this.roles = new HashSet<>();
  }

  public User(
      String username,
      String password
  ){

    this();
    this.username = username;
    this.password = password;
  }

  public User(
      String username,
      String password,
      List<Role> roles
  ) {

    this(username, password);
    addRoles(roles);
  }

  public User addRole(Role role) {
    this.roles.add(role);
    return this;
  }

  public void addRoles(List<Role> roles) {
    var validatedRoleSet = Role.validateRoleSet(roles);
    this.roles.addAll(validatedRoleSet);
  }
}