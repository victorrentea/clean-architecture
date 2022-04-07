package victor.training.clean.domain.service;

import victor.training.clean.domain.entity.User;

public interface IAdapter {
   User retrieveUser(String username);
}
