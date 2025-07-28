package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String fullName,
    Optional<String> email,
    String username
) {
//  public static User fromLdap(LdapUserDto dto) {..}
}
