package victor.training.clean.domain.model;


import java.util.Optional;

public class User {
    public User(String userName, String email, String fullName) {
        this.userName = userName;
        this.email = email;
        this.fullName = fullName;
    }

    private final String userName;
    private final String email;
    private final String fullName;

    public Optional<String> getEmailContact() {
      return getEmail().map(e -> fullName + " <" + e + ">");
    }

    public String getUserName() {
        return userName;
    }

    public Optional <String> getEmail() {
        return Optional.ofNullable(email).map(String::toLowerCase);
    }

    public String getFullName() {
        return fullName;
    }
}
