package victor.training.clean.domain;

import victor.training.clean.domain.model.User;

public interface ILdapUserAdapter {
  User fetchUserFromLdap(String usernamePart);
}
