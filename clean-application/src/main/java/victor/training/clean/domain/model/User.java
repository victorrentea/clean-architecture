package victor.training.clean.domain.model;

import java.util.Optional;

// modelul meu. AL MEU.
// din problem a mea
// ce-mi trebuie din lumea CORUPTA externa
public record User(
    String username,
    String fullName,
    Optional<String> email
) {
}
