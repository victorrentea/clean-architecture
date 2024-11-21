package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String username,
    String fullName,
    Optional<String> email
) {
  public Optional<String> contact() {
    return email.map(e -> fullName + " <" + e + ">");
  }

//  public String usernameForDisplay() { // infra mapping logic
//    return username.startsWith("s") ? "system" : username;
//  }
}
