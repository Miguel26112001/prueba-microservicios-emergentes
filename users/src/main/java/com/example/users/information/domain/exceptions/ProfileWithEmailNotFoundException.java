package com.example.users.information.domain.exceptions;

public class ProfileWithEmailNotFoundException extends ProfileNotFoundException {

  public ProfileWithEmailNotFoundException(String email) {
    super("Profile with email '" + email + "' not found");
  }
}
