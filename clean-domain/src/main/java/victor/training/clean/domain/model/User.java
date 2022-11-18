package victor.training.clean.domain.model;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

// Value Object = small, immutable, PK-free object
public class User {
  private final String username;
  private final String fullName;
  private final String email;

  public User(String username, String fullName, String email) {
    this.username = requireNonNull(username);
    this.fullName = requireNonNull(fullName);
    this.email = email;
  }

  // the next big util tomorow: UserUtil
  public Optional<String> asContact() {
    return getEmail().map(e->fullName + " <" + e + ">");
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
