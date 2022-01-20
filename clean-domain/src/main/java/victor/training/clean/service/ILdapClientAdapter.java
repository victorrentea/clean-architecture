package victor.training.clean.service;

import victor.training.clean.entity.User;

import java.util.List;

public interface ILdapClientAdapter {
   List<User> searchByUsername(String username);
}
