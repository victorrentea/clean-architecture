package victor.training.clean.domain.service;

import victor.training.clean.domain.service.User;

public interface ExternalUserProvider {
  User fetchByUsername(String username);
}
