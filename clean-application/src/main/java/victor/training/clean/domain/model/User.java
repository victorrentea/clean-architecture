package victor.training.clean.domain.model;

import java.util.Optional;

/**
 * What my business logic needs to know about a user.
 * Immutable, no Lombok, no trace of LDAP: fields shaped for the logic, not for the wire.
 */
public record User(
    String username,
    String fullName,
    Optional<String> workEmail
) {
  /** Empty if the user has no work email: absence is a normal case here, not an error. */
  public Optional<String> asEmailContact() {
    return workEmail.map(email -> fullName + " <" + email + ">");
  }
}
