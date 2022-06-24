package victor.training.clean.domain.insurance.service;

import victor.training.clean.domain.insurance.entity.User;

public interface ExternalUserProvider {
    User retrieveUser(String username);
}
