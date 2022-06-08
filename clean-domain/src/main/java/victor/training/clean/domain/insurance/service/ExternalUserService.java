package victor.training.clean.domain.insurance.service;

import victor.training.clean.domain.insurance.model.User;

public interface ExternalUserService {
//    LdapUserDto mistake();
    User getUserByUsername(String username);
}
