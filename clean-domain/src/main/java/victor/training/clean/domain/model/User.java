package victor.training.clean.domain.model;

import java.util.Optional;

// Value Object
public record User(
    String username,
    String fullName,
    Optional<String> email
) {
}
