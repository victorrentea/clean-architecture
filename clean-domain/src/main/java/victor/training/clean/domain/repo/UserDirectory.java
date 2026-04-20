package victor.training.clean.domain.repo;

import victor.training.clean.domain.model.User;

public interface UserDirectory {
  User findByUsernamePart(String usernamePart);
}
