package victor.training.clean.infra;

import victor.training.clean.domain.model.User;

public interface LdapUserClientInterface {
  User fetchUserByUsername(String username);
}
