package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface IUserFetcher {
  User fetchUser(String usernamePart);
}
