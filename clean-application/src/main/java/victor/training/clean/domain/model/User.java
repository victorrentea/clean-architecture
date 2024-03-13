package victor.training.clean.domain.model;

import java.util.Optional;

public record User(String username, String fullName, Optional<String> email) {
  public boolean isInternalEmail() {
//    return email().get().toLowerCase().endsWith("@cleanapp.com");
    return email().map(e -> e.toLowerCase().endsWith("@cleanapp.com")).orElse(false);
  }
}
