package victor.training.clean.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class User {
    private final String username;
    private final String corporateName;
    private final String workEmail;

    public User(String username, String corporateName, String workEmail) {
        this.username = requireNonNull(username);
        if (username.length() < 5) {
            throw new IllegalArgumentException("VALEU");
        }
        this.corporateName = corporateName;
        this.workEmail = workEmail;
    }

    public boolean hasWorkEmail() {
        return workEmail != null;
    }

    public String getUsername() {
        return username;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public String getWorkEmail() {
        return workEmail;
    }
}
