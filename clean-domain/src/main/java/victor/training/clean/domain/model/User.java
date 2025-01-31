package victor.training.clean.domain.model;

import java.util.Optional;

// VALUE OBJECT
public record User(
    String name,
    Optional<String> email, // ok if missing
    String username
) {
  public Optional<String> asContact() {
    return email.map(e -> name + " <" + e.toLowerCase() + ">");
  }
}

//middle ground: on-the-fly conversion from incoming Json
//class User2 {
//  private String fName; // JACKSON doesn't need getter/setters
//  private String lName;
//  private String workEmail;
//  private String un;
//
//  public String fullName() {
//    return fName + " " + lName.toUpperCase();
//  }
//  public Optional<String> email() {
//    return Optional.ofNullable(workEmail);
//  }
//  public String username() {
//    return un.startsWith("@") ? un : "@" + un;
//  }
//
//}

