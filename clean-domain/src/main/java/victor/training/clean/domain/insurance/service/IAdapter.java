package victor.training.clean.domain.insurance.service;

import victor.training.clean.domain.insurance.entity.User;

public interface IAdapter {
    User retrieveUser(String username);
}
