package victor.training.clean.domain.model;

import lombok.Value;

import java.util.Optional;

public record User(
    String fullName,
    Optional<String> email,
    String username
    // private campAscuns
) {
  //  public String asContact() {
//    if (email.isEmpty()) return null;
//    return fullName + " <" + email.get().toLowerCase() + ">";
//  }
  public Optional<String> asContact() {
    return email.map(e -> fullName + " <" + e.toLowerCase() + ">");
  }
}

//@Value
//public class User {
//  String fullName;
//  String email;
//  String username;
//
//  public Optional<String> getEmail() {
//    return Optional.ofNullable(email);
//  }
//}


////urat in @Test sa lucrezi cu DTO externe
//class UserWrapper {
//  private LdapUserDto dto;
//  public String getFullName() {
//    return dto.getFname() + " " + dto.getLname().toUpperCase();
//  }
//  public String getEmail() {
//    return dto.getWorkEmail();
//  }
//
//}
