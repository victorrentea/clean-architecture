package victor.training.clean.domain.model;

import victor.training.clean.infra.LdapUserDto;

import java.util.Optional;

// data ckass val...
public record User(
    String username,
    String fullName,
    Optional<String> email
) {
}

// cumbersome to use in unit-tests.
// to create it i have to instatiate the ugly DTO.
//class UserWrapper {
//  private final LdapUserDto dto;
//
//  UserWrapper(LdapUserDto dto) {
//    this.dto = dto;
//  }
//  public String getFullName() {
//    return dto.getFname() + " " + dto.getLname().toUpperCase();
//  }
//}
