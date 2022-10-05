package victor.training.clean.domain.model;

import java.util.Objects;

public class User {
    private final String username;
    private final String workEmail;
    private final String fullName;

    public User(String username, String workEmail, String fullName) {
        this.username = Objects.requireNonNull(username);
        this.workEmail = workEmail;
        this.fullName = fullName;
    }

    public boolean hasWorkEmail() {
       return getWorkEmail() != null;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getWorkEmail() {
        return workEmail;
    }
}
