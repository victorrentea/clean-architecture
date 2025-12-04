package victor.training.clean.domain.model;

// Domain Value Object representing the minimal user info needed by core logic
public final class User {
  private final String username;
  private final String fullName;
  private final String workEmail; // optional, may be null

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

  public String getWorkEmail() {
    return workEmail;
  }
}
