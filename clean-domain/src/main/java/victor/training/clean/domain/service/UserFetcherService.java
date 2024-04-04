package victor.training.clean.domain.service;

import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.User;

public interface UserFetcherService {
  // SHIT in DOMAIN package
  User fetchUser(Customer customer, String usernamePart);
}
