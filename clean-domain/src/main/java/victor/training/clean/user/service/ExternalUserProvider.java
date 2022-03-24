package victor.training.clean.user.service;

import victor.training.clean.user.entity.User;

public interface ExternalUserProvider {
   User loadUser(String username);
//   void saveUser(User user); // dangerous to leak out my internal entity
}
