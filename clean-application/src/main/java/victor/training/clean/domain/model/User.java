package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String fullName,
    String username,
    Optional<String> email) {
  public Optional<String> asContact() {
    return email.map(e -> fullName + " <" + e.toLowerCase() + ">");
  }
}
