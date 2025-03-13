package victor.training.clean.domain.model;

import java.util.Optional;

// VALUE OBJECT = IMUTABIL si nu are PK (identitate persistenta)
public record User(
    String fullName,
    Optional<String> email,
    String username
) {
}
