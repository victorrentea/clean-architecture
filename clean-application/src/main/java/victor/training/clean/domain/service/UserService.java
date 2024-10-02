package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface UserService { // part of the ubiquitous language
  // spoken by everyone (including the business expert, tester, BA)
  User fetchUserByUsername(String usernamePart);
}
