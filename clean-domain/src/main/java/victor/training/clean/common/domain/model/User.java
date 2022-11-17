package victor.training.clean.common.domain.model;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

//Value Object = small, immutable and hash/eq on all fields. no persistent identity.
public class User {
  private final String username;
  private final String email;
  private final String fullName;

  public User(String username, String email, String fullName) {
    this.username = requireNonNull(username);
    this.email = email;
    this.fullName = fullName;
  }

  public String getFullName() {
    return fullName;
  }

  public String getUsername() {
    return username;
  }

  public Optional<String> getEmail() {
    return ofNullable(email);
  }
}
