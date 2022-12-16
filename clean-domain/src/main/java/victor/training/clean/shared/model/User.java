package victor.training.clean.shared.model;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

// value object: imutabil, mic,
// daca se poate: constrans
public class User {
  private final String username;
  private final String email;
  private final String fullName;

  public User(String username, String email, String fullName) {
    this.username = username!=null?username:"anonymous";
    this.email = email;
    this.fullName = requireNonNull(fullName);
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

  public Optional<String> getEmailContact() {
    return getEmail().map(e -> fullName + " <" + e + ">");
  }
}
