package victor.training.clean.domain.model;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class User {
  private final String username;
  private final String fullName;
  private final String email;

  public User(String username, String fullName, String email) {
    this.username = requireNonNull(username);
    this.fullName = requireNonNull(fullName);
    this.email = email;
  }

  // shared behavior on external data
  public boolean hasInternalEmail() {
    return getEmail()
        .map(String::toLowerCase)
        .map(s -> s.endsWith("@cleanapp.com"))
        .orElse(false);
  }
// introduces couplping to the external DTO from a DOmain object
  // instantiating this object in @Tests will force you to first create an external UGLY Dto
//  public User(LdapUserDto userDto) {
//    String fullName = userDto.getFname() + " " + userDto.getLname().toUpperCase();
//    String username = userDto.getUn();
//    if (username.startsWith("s")) username = "system";
//    this(username, fullName, userDto.getWorkEmail());
//  }

  // null-safe
  public Optional<String> getEmail() {
    return ofNullable(email);
  }

  // good names
  public String getUsername() {
    return username;
  }

  // initial mapping
  public String getFullName() {
    return fullName;
  }
}
