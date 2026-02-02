package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface UserApi {
  //  public LdapUserDto method() { // leak out of the infra back into domain // ILLEGAL
//
//  }
  User retrieveUser(String usernamePart);
}
