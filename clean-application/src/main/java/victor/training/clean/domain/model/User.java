package victor.training.clean.domain.model;

import java.util.Optional;
// VALUE OBJECT = immutable small, lacking continuity change
public record User(String username,
                   String fullName,
                   Optional<String> email) {}

// Refactoring Level 1 = Code smells
// Refactoring Level 2 = Restructuring Domain
//