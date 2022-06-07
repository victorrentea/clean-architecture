package victor.training.clean.domain.service;

import victor.training.clean.domain.entity.User;

public interface ExternalUserService {
//    LdapUserDto mistake();
    User getUserByUsername(String username);
}
