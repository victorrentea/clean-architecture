package victor.training.clean.domain.model;


public class User {
    public User(String userName, String email, String fullName) {
        this.userName = userName;
        this.email = email;
        this.fullName = fullName;
    }

    private final String userName;
    private final String email;
    private final String fullName;

    public String getEmailContact() {
      return fullName + " <" + email.toLowerCase() + ">";
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
