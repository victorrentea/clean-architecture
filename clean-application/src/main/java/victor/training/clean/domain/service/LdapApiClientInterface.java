package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface LdapApiClientInterface {
  User loadUserByUsername(String targetUsername);
}
