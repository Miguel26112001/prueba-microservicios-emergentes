package com.example.users.information.domain.model.valueobjects;

import com.example.users.information.domain.exceptions.InvalidUserIdException;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(
    Long value
) {

  public UserId {
    if (value == null || value <= 0) {
      throw InvalidUserIdException.forId(value);
    }
  }
}