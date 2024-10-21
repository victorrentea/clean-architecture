package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface UserApiClient {
  // Dirty
  User fetchByUsername(String usernamePart);
}
