package victor.training.clean.domain.model;

import lombok.Builder;

import java.util.Optional;

public record User(
    String fullName,
    Optional<String> email,
    String username
    ) {
}
