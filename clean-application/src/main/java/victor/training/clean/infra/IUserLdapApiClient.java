package victor.training.clean.infra;

import victor.training.clean.domain.model.User;

public interface IUserLdapApiClient {
  User fetchUser(String username);
}
