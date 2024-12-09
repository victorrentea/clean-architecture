package victor.training.clean.domain.model;

import java.util.Optional;

// Domain Value Object, to which I map an external DTO
// DTO = API model
public record User(
    String username,
    String fullName,
    // the only exception to the rule of
    // DON'T have Optionals on field or parameter
    // ie. Java Sucks!
    Optional<String> workEmail
) {

  // embedding mapping into the structure
  // more natural to happen from the beginning, from the mapper
//  public String normalizedUsername() {
//    if (username.startsWith("s")) {
//      return "system";
//    } else {
//      return username;
//    }
//  }
}
