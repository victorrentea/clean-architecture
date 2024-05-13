package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String username,
    String fullName,
    Optional<String> email
) {
  public Optional<String> asEmailRecipient() {
    return email().map(e -> fullName + " <" + e + ">");
  }
}
