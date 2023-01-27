package victor.training.clean.domain.model;


import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

// Value Object la care voi mapa datele din Dto extern
//=love e mai bun ca @Data = hate
// imutable
public final class User {
  private final String username;
  private final String fullName;
  private final String email;

  public User(String username, String fullName, String email) {
    this.username = requireNonNull(username);
    this.fullName = requireNonNull(fullName);
    this.email = email;
  }

  public Optional<String> getEmail() {
    return ofNullable(email);
  }

  //EmailUtil {}
  public String asEmailContact() {
    return getFullName() + " <" + getEmail() + ">";
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public String toString() {
    return "User(username=" + getUsername() + ", fullName=" + getFullName() + ", email=" + getEmail() + ")";
  }
}
