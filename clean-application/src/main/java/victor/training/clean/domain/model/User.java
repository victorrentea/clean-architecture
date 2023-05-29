package victor.training.clean.domain.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Optional;

@Entity
@Builder
@Data
public class User {

    String userName;

    String fullName;

    String workEmail;

    public Optional<String> getWorkEmail() {
        return Optional.of(workEmail);
    }

    public String getToAddress() {
        return fullName + " <" + workEmail + ">";
    }
}
