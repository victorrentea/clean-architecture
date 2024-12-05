package victor.training.clean.domain.model;

import lombok.NonNull;

import java.util.Optional;

public record User(
    @NonNull
    String username,
    @NonNull
    String fullName,
    Optional<String> email // onl;y place in Java allowed
) {
  public Optional<String> asContact() {
    return email.map(e -> fullName + " <" + e + ">");
  }

//  User(LdapUserDto dto)
}
