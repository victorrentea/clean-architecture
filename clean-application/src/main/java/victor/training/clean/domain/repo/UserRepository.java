package victor.training.clean.domain.repo;

import victor.training.clean.domain.model.User;

public interface UserRepository {
  User findSingleUserByUsernamePart(String usernamePart);
}
