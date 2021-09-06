package victor.training.clean.customer.service;

import victor.training.clean.customer.entity.User;

import java.util.List;

public interface ExternalUserProvider {
   List<User> searchByUsername(String username);
}
