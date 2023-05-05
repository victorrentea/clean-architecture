package victor.training.clean.domain.model;

public class User {

    private final String userRct;
    private final String email;
    private final String fullName;

    public User(String userRct, String email, String fullName) {
        this.email = email;
        this.userRct = userRct ;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserRct() {
        return userRct;
    }

    public String getFullName() {
        return fullName;
    }
}
