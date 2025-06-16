package victor.training.clean.domain.model;

import java.util.Optional;

//Value Object never persisted
public record User(
    String username,
    String fullName,
    Optional<String> email
) {
  // the stuff WE NEED from outside
}
