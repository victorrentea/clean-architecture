package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface UserProvider {
  User retrieveUser(String userId);
}
