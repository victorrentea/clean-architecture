package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;
// there are only two things hard in programming:
// cache invalidation and naming things
public interface UserRetriever {
  User retrieveUser(String usernamePart);
}
