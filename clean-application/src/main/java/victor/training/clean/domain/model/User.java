package victor.training.clean.domain.model;

import jakarta.validation.constraints.Email;

public record User(
    Username username,
//    Username username, // wrap over key ID types

//    FullName fullName,// NO!
    String fullName,

    @Email
    String email //
//    Email email, // maybe .domain():String, .isInternal(){returns if it ends in @css.ch} or constraints
) {
}
//record FullName(String value) {}

