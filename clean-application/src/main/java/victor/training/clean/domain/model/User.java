package victor.training.clean.domain.model;

import java.util.Optional;

public class User {

    private final String userRct;
    private final String email;
    private final String fullName;

    public User(String userRct, String email, String fullName) {
        this.email = email;
        this.userRct = userRct ;
        this.fullName = fullName;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public String getUserRct() {
        return userRct;
    }

    public String getFullName() {
        return fullName;
    }
}
