package victor.training.clean.domain.model;

import java.util.Optional;

// VALUE OBJECT mapping external 💩 in my 🧘 world
public record User(
    String fullName,
    Optional<String> email,
    String username
) {
}
