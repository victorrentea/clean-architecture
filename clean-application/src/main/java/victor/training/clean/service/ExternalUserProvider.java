package victor.training.clean.service;

import victor.training.clean.entity.User;

public interface ExternalUserProvider {
   User findOneUserByUsername(String username);
}
