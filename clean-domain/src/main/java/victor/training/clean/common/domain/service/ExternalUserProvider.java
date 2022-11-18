package victor.training.clean.common.domain.service;

import victor.training.clean.common.domain.model.User;

public interface ExternalUserProvider {
  User fetchUserByUsername(String username);
}
