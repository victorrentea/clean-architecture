package victor.training.clean.service;

import victor.training.clean.entity.User;

public interface ExternalUserProvider {
   User getOneUserByUsername(String username);
//   LdapUserDto getOneUserByUsername2(String username);
}
