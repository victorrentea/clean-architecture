package victor.training.clean.domain.service;

import org.checkerframework.checker.nullness.qual.NonNull;
import victor.training.clean.domain.model.User;

public interface UserApi {
  //  public LdapUserDto method() { // leak out of the infra back into domain // ILLEGAL
//
//  }
  @NonNull
  User retrieveUser(String usernamePart);
}
