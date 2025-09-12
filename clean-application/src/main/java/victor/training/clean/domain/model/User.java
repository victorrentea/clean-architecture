package victor.training.clean.domain.model;

import victor.training.clean.infra.LdapUserDto;

public record User(
  String fullName,
  String email,
  String username
) {
}

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
