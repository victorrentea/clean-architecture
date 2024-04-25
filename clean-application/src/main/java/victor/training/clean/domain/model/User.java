package victor.training.clean.domain.model;

import victor.training.clean.infra.LdapUserDto;

import java.util.Optional;

public record User(
    String username,
    String fullName,
    Optional<String> email// only place where optional is acceptable for field/param
) {
}
