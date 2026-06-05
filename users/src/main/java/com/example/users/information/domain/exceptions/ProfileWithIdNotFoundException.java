package com.example.users.information.domain.exceptions;

public class ProfileWithIdNotFoundException extends ProfileNotFoundException {

  public ProfileWithIdNotFoundException(Long userId) {
    super("Profile with id '" + userId + "' not found");
  }
}
