package victor.training.clean.domain.model;

// Value Object domain
// Value Object = immutable, no identity, no lifecycle
// versus Entity = mutable, has identity, has lifecycle
public record User(
    String username,
//    String normalizedUsername,
    String fullName,
    String email
) {
}
