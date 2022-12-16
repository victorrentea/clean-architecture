package victor.training.clean.shared.service;

import victor.training.clean.shared.model.User;

public interface ILdapApiAdapter {
  User fetchUserByUsername(String username);
}
