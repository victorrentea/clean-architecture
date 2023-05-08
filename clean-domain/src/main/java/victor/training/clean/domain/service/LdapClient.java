package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface LdapClient  {
  User retrieveUser(String username);
}
