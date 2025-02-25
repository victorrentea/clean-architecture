package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String username,
    String fullName,
    Optional<String> email
) {
  public Optional<String> asContact() {
//    return fullName + " <" + email().get().toLowerCase() + ">";
    return email.map(e -> fullName + " <" + e.toLowerCase() + ">");
  }

//  @Override
//  public Optional<String> email() { // doesn;t compile
//    return Optional.ofNullable(email);
//  }
}
