package victor.training.clean.domain.model;

import java.util.Optional;

//  Value Object = Immutable, no PK
public record User(
    String username,
    Optional<String> email,
    String fullName) {
}
