package victor.training.clean.domain.repo;

import victor.training.clean.domain.model.User;

public interface UserRepository {
  /**
   * Finds a single user by a username fragment as provided by the caller.
   * Implementations may adjust the search (eg. uppercase) as needed.
   * Must throw IllegalArgumentException if not exactly one user is found.
   */
  User findSingleUserByUsernamePart(String usernamePart);
}
