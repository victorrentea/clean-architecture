package victor.training.clean.domain.user.service;

import victor.training.clean.domain.user.model.User;

public interface ExternalUserProvider {
    User retrieveByUsername(String username);
}
