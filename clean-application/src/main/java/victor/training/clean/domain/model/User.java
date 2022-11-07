package victor.training.clean.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

// Value Object =
// - immutable
// - no persistent identity (vs Entity PK)
// - valid in my Domain
// - can contain logic
// - hash/eq involves all fields
@Value
public class User {
    @NonNull
    String username;
    String workEmail;
    String fullName;

//    public User(String username, String workEmail, String fullName) {
//        this.username = requireNonNull(username);
//        this.workEmail = workEmail;
//        this.fullName = fullName;
//    }

    public Optional<String> getWorkEmail() {
        return Optional.ofNullable(workEmail);
    }
}


//class Username {
//    private final String value;
//
//    Username(String value) {
//        if (value.length() > 3) {
//            throw new IllegalArgumentException();
//        }
//        this.value = requireNonNull(value);
//    }
//}