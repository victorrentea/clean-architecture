package victor.training.clean.domain.model;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

// restTemplate.getForObject(UserPragmatic.clas)
public class UserPragmatic {
//  @JsonProperty("uid")
  private String username;
  private String email;
  private String first;
  private String last;
  protected UserPragmatic() {}
//  protected UserPragmatic() {}


  public Optional<String> getEmail() {
    return ofNullable(email);
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return first + " " + last.toUpperCase();
  }
}

// new UserPragmaticComposing(restTemplate.getForObject(LdapUserDto.clas))
// in your tests : new UserPragmaticComposing(new foreign Dto)
//
//public class UserPragmaticComposing {
//
//  private LdapUserDto dto; // generate
//
//  public Optional<String> getEmail() {
//    return ofNullable(email);
//  }
//
//  public String getUsername() {
//    return dto.getUid();
//  }
//
//  public String getFullName() {
//    return first + " " + last.toUpperCase();
//  }
//}
