package victor.training.clean.domain.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Optional;

@Entity
@Data
public class User {

    private String userName;

    private String fullName;

    private String workEmail;

    public User(String userName, String workEmail, String firstName, String lName) {
        this.userName = userName != null ? userName.toLowerCase() : "anonymous";
        this.workEmail = workEmail != null ? workEmail.toLowerCase() : null;
        String lastName = lName != null ? lName.toUpperCase() : "";
        this.fullName = firstName + " " + lastName;
    }

    public Optional<String> getWorkEmail() {
        return Optional.of(workEmail);
    }

    public String getToAddress() {
        return fullName + " <" + workEmail + ">";
    }

}
