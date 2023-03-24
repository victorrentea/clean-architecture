package victor.training.clean.domain.model;

import java.util.Objects;
import java.util.Optional;

public class User {
  private final String username;
  private final String email;
  private final String fullName;

  public User(String username, String email, String fullName) {
    this.username = Objects.requireNonNull(username);
    this.email = email;
    this.fullName = fullName;
  }

  public Optional<String> asEmailContact() {
    return getEmail().map(email->fullName + " <" + email.toLowerCase() + ">");
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public Optional<String> getEmail() {
    return Optional.ofNullable(email);
  }
}
