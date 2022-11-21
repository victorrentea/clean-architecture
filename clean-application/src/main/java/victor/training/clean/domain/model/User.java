package victor.training.clean.domain.model;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

// value object:
// - small (1-5-7)
// - immutable
// - no persistent identity PK
// - hash/eq on all fields

public class User {
  private final String username;
  private final String email;
  private final String fullName;

  public User(String username, String email, String fullName) {
    this.username = requireNonNull(username);
    this.email = email;
    this.fullName = requireNonNull(fullName);
  }

  public Optional<String> asContact() {
    return ofNullable(email).map(e -> fullName + " <" + e + ">");
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public Optional<String> getEmail() {
    return ofNullable(email);
  }

}
