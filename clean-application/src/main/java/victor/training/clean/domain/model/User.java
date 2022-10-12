package victor.training.clean.domain.model;

import java.util.Objects;
import java.util.Optional;

// ValueObject
public class User { // = immutable, mic, hash/equals implica toate campurile.
    private final String username;
    private final String corporateName;
    private final String email;

    public User(String username, String corporateName, String email) {
        this.username = Objects.requireNonNull(username);
        this.corporateName = corporateName;
        this.email = email;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public String getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }
}
