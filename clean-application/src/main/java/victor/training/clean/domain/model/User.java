package victor.training.clean.domain.model;

import java.util.Optional;

import static java.util.Optional.ofNullable;

//@Value // Value Object = small immutable without PK
public class User {
  private final String username;
  private final String fullName;
  private final String email;

  public User(String username, String fullName, String email) {
    this.username = username;
    this.fullName = fullName;
    this.email = email;
  }

  public Optional<String> asEmailContact() {
    return getEmail().map(e -> fullName + " <" + e + ">");
  }

  public String getFullName() {
    return fullName;
  }

  public Optional<String> getEmail() {
    return ofNullable(email);
  }

  public String getUsername() {
    return username;
  }
}
