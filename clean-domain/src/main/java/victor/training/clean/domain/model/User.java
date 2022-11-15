package victor.training.clean.domain.model;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

// TESTING is MUCH easier here
public class User {
  private final String username;
  private final String email;
  private final String fullName;

  public User(String username, String email, String fullName) {
    this.username = requireNonNull(username);
    this.email = email;
    this.fullName = fullName;
  }

  public Optional<String> getEmail() {
    return ofNullable(email);
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }
}
