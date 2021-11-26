package victor.training.clean.user.service;

import victor.training.clean.user.entity.User;

public interface ILdapUserService {

   User findSingleUserByUsername(String username);
}
