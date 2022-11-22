package victor.training.clean.shared.domain.service;

import victor.training.clean.shared.domain.model.User;

public interface ExternalUserProvider {
  User fetchUserByUsername(String username);
}
