package victor.training.clean.domain.model;

import java.util.Optional;

public record User(String username, String fullName, Optional<String> workEmail) {

  public User {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("username must be non-null and non-blank");
    }
    if (fullName == null || fullName.isBlank()) {
      throw new IllegalArgumentException("fullName must be non-null and non-blank");
    }
    if (workEmail == null) {
      throw new IllegalArgumentException("workEmail Optional must not be null");
    }
    workEmail.ifPresent(s -> {
      if (s == null) {
        throw new IllegalArgumentException("workEmail must not contain null");
      }
    });
  }
}
