package victor.training.clean.domain.model;

import jakarta.validation.constraints.Email;
import victor.training.clean.infra.LdapUserDto;

import java.util.Optional;

// Value Object
public record User (
    String username,
    String fullName,
    Optional<String> email // Remarkable, but ok
) {

  // this breaks the Dependency Rule as it couples something from my Domain to the outside world (DTO from a 3rd party API)
//  public static User fromLdapUser(LdapUserDto dto) {
//  }
}
