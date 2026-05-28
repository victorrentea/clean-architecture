package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String firstName,
    String lastName,
    String username,
    Optional<String> workEmail
) {
  public String fullName() {
    return firstName + " " + lastName.toUpperCase();
  }
}
