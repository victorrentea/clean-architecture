package victor.training.clean.domain.model;

import java.util.Optional;

// value object = immutable, equals/hashCode, no PK (no ID)
public record User(String username,
                   Optional<String> email,
                   String fullName) {}
