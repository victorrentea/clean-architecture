package victor.training.clean.service;

import victor.training.clean.entity.User;

public interface ExternalUserProvider {
   User loadUser(String username);
//   void saveUser(User user); // dangerous to leak out my internal entity
}
