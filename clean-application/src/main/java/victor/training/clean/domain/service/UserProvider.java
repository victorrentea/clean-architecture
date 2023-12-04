package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface UserProvider {

  User fetchUser(String username);
  // fetch warns the caller that this costs time and network!! WARNING!!!!
}
