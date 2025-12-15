package victor.training.clean.domain.model;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public record User(
        String username,
        String fullName,
        Optional<String> workEmail) {
}
