package victor.training.clean.domain.model;

public class User {

    private final String workEmail;
    private final String userRct;
    private final String firstName;
    private final String lastName;

    public User(String workEmail, String userRct, String firstName, String lastName) {
        this.workEmail = workEmail;
        this.userRct = userRct;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public String getUserRct() {
        return userRct;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
