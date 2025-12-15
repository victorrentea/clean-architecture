package victor.training.clean.domain.repo;

import victor.training.clean.domain.model.User;

public interface UserRepo {
  User search(String usernamePart);
}
