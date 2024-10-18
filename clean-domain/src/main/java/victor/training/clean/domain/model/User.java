package victor.training.clean.domain.model;

import java.util.Optional;

// Value Object = immutable and without persistent life
public record User (
    String username,
    String fullName, // 💋
    Optional<String> email
    // we shall not use the optional as
    // method parameters or field types except in record
){
}
