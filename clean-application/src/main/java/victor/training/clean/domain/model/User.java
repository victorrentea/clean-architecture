package victor.training.clean.domain.model;

import java.util.Optional;

// Value Object
public record User(
    String username,
    String fullName,
    Optional<String> workEmail
) {
  public boolean isMyDomain() {
    return workEmail()
        .map(email -> email.toLowerCase().endsWith("@cleanapp.com"))
        .orElse(false);
  }
}
