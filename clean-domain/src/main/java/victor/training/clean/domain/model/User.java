package victor.training.clean.domain.model;

import java.util.Optional;
// VO
public record User(
    String username,
    String fullName,
    Optional<String> email
) {
//  public String asContact() {
//    return fullName() + " <" + email().get().toLowerCase() + ">";
//  }
  public Optional<String> asContact() {
    return email.map(e -> fullName + " <" + e.toLowerCase() + ">");
  }
}
