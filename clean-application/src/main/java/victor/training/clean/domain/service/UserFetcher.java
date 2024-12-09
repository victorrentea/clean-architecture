package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface UserFetcher {
  // this method's complexity is more about mapping. shouldn't it be called mapUser()
  // every time you name SOMETHING, think of that name from its caller's perspective
  User fetchUser(String usernamePart);
}
