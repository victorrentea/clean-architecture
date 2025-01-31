package victor.training.clean.domain.model;

import java.util.Optional;

// VALUE OBJECT
public record User(
    String name,
    Optional<String> email, // ok if missing
    String username
) {
  public Optional<String> asContact() {
    return email.map(e -> name + " <" + e.toLowerCase() + ">");
  }
}
