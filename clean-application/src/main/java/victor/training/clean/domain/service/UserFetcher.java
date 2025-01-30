package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

// there are only two things hard in programming 🤔:
// naming things, cache invalidation and off-by-one errors

public interface UserFetcher { // Output Port
  User fetchUser(String usernamePart);
}
