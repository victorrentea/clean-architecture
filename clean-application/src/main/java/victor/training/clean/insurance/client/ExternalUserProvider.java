package victor.training.clean.insurance.client;

import victor.training.clean.insurance.domain.model.User;

public interface ExternalUserProvider {
  User fetchByUsername(String targetUsername);
}
