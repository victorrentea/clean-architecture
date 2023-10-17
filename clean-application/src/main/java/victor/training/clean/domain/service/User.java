package victor.training.clean.domain.service;


import victor.training.clean.infra.LdapUserDto;

// obiectul MEU!
public class User {
//  private final LdapUserDto dtoOriginal;
  private final String username; // in loc de un
  private final String fullName; // in loc de fName + " " + lName.upper
  private final String email; // workEmail

  public User(String username, String fullName, String email) {
    if (username == null) {
      throw new IllegalArgumentException();
    }
    this.username = username;
    this.fullName = fullName;
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public String getFullName() {
    return fullName;
  }

  public String getUsername() {
    return username;
  }
}
