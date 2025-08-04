package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String fullName,
    Optional<String> email,
    String username
) {
  public Optional<String> asContact() {
    return email.map(e -> fullName + " <" + e.toLowerCase() + ">");
  }
}
