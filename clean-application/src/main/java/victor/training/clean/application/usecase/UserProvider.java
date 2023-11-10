package victor.training.clean.application.usecase;

import victor.training.clean.application.entity.User;

public interface UserProvider {
  User fetchUser(String userId);
}
