package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

import java.util.Optional;

@Embeddable
public record User(
    String username,
    String fullName,
    Optional<String> email
) {
}
