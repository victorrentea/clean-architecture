package victor.training.clean.service;

import victor.training.clean.entity.User;

public interface ILdapUserService {
   User findSingleUserByUsername(String username);
}
