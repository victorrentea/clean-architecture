package victor.training.clean.domain.model;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class User {
  private final String username;
  private final String email;
  private final String fullName;

  public User(String username, String email, String fullName) {
    this.username = requireNonNull(username);
    this.email = email;
    this.fullName = requireNonNull(fullName);
  }

  public Optional<String> asEmailContact() {
    return getEmail().map(e->fullName + " <" +e + ">");
  }

  public Optional<String> getEmail() {
    return ofNullable(email);
  }

  public String getFullName() {
    return fullName;
  }

  public String getUsername() {
    return username;
  }
}
