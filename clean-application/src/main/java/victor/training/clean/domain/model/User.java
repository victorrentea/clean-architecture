package victor.training.clean.domain.model;

import jakarta.validation.constraints.Email;
import lombok.Value;

import java.util.Optional;

//@Value
public record User(
    Username username,
//    Username username, // wrap over key ID types

//    FullName fullName,// NO!
    String fullName,

    @Email
    Optional<String> email // the only place where Optional is valid as argument in Java language
//    Email email, // maybe .domain():String, .isInternal(){returns if it ends in @css.ch} or constraints
) {
//    public Optional<String> emailOpt() {
//        return Optional.ofNullable(email);
//    }
}
//record FullName(String value) {}

// or (3)
// @Value class User{ String email; public Optional<String> getEmail() { return Optional.ofNullable(email); } }