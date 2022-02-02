package victor.training.clean.service;

import victor.training.clean.entity.User;

public interface ILdapServiceAdapter {
   User searchOneByUsername(String username);
}
