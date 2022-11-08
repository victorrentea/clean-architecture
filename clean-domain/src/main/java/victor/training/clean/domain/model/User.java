package victor.training.clean.domain.model;

import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

// Value Object =
// - immutable
// - no persistent identity (vs Entity PK)
// - valid in my Domain
// - can contain **PURE FUNCTION** logic
// - hash/eq involves all fields
//@Value
//public class User {
//    @NonNull
//    String username;
//    String workEmail;
//    String fullName;
////    Customer customer; // descouraged to have VO -> Entities.
//
//    // side-effecting function < NOT ALLOWED in a VO
////    public void changeAnything() {
//////        username = "new";
////        customer.setSite(null);
////    }
//
////    @Json...
////    @NotNull
////    @Pattern()
////    private String fName;
////    private String lName;
//
////    public String getFullName() {
////        return fName + " " + lName.toUpperCase();
////    }
//
////    public User(String username, String workEmail, String fullName) {
////        this.username = requireNonNull(username);
////        this.workEmail = workEmail;
////        this.fullName = fullName;
////    }
//
//    public Optional<String> getWorkEmail() {
//        return Optional.ofNullable(workEmail);
//    }
//}

public record User(String username,
            Optional<String> workEmail,
            String fullname) {

//    public void method() {
//        work
//    }
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