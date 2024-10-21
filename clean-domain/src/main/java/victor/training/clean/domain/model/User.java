package victor.training.clean.domain.model;


import java.util.Optional;

public record User(
    String username,
    String fullName,
    Optional<String> email
) {

}

//class UserWrapper { // hard to use in unit tests: will have to create a Dto
//  private final LdapUserDto dto;
//
//  UserWrapper(LdapUserDto dto) {
//    this.dto = dto;
//  }
//  public String fullName() {
//    return dto.getFname() + " " + dto.getLname().toUpperCase();
//  }
//
//  public LdapUserDto getDto() {
//    return dto;
//  }
//}
