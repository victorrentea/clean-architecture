package victor.training.clean.application.port;

import victor.training.clean.application.entity.User;

public interface UserProviderPort {
  User fetchUser(String userId);
}
