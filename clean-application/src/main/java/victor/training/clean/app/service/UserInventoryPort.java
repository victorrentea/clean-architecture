package victor.training.clean.app.service;

import victor.training.clean.app.model.User;

public interface UserInventoryPort {
  User fetchUser(String usernamePart);
}
