package victor.training.clean.core.service;

import victor.training.clean.core.model.User;

public interface UserInventoryPort {
  User fetchUser(String usernamePart);
}
