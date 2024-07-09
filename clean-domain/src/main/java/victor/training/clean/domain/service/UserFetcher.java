package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface UserFetcher {
  User fetchUserByUsername(String usernamePart);
}
