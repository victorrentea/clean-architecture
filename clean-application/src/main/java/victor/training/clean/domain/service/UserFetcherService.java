package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface UserFetcherService {
  User fetchUser(String usernamePart);
}
