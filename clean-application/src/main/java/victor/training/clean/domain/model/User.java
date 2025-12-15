package victor.training.clean.domain.model;

import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Small domain value object representing a user identity coming from LDAP.
 */
public final class User {
  private final String username; // normalized username
  private final String fullName; // eg: "John DOE"
  private final String workEmail; // may be null

  public User(String username, String fullName, String workEmail) {
    this.username = username;
    this.fullName = fullName;
    this.workEmail = workEmail;
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public Optional<String> getWorkEmail() {
    return ofNullable(workEmail);
  }
}
