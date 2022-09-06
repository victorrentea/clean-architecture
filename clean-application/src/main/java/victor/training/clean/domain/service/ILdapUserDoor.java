package victor.training.clean.domain.service;

import victor.training.clean.domain.entity.User;

public interface ILdapUserDoor {
    User retrieveByUsername(String username);
}
