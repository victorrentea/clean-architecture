package victor.training.clean.domain.service;

import victor.training.clean.domain.entity.User;

public interface LdapService {
   User loadUser(String username);
}
