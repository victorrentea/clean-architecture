package victor.training.clean.service;

import victor.training.clean.entity.User;

public interface IAdapter {
   User getUserFromLdap(String username);
}
