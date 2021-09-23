package victor.training.clean.service;

import victor.training.clean.customer.entity.User;

import java.util.List;

public interface ExternalUserService {
   List<User> searchByUsername(String username);
}
