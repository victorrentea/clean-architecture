package victor.training.clean.user.service;

import victor.training.clean.user.entity.User;

public interface LdapService {
   User loadUser(String username);
}
