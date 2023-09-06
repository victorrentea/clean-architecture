package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface ILdapClient {
  // the public api of the adapter does not LEAK external concepts (eg LdapUserDto). Only uses my domain objects!!! (or primitives)
  User loadUserFromLdap(String userId);
}
