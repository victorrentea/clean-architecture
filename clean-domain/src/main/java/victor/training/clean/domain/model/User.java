package victor.training.clean.domain.model;

// holding the strictly necessary properties
public record User(
    String username,
    String fullName,
    String email
) {
}
