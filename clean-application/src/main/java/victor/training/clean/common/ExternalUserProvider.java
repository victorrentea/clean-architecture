package victor.training.clean.common;

import victor.training.clean.common.entity.User;

public interface ExternalUserProvider {
  User retrieveUserById(String username);
}
