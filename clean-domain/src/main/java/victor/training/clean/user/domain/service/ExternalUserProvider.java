package victor.training.clean.user.domain.service;

import victor.training.clean.user.domain.model.User;

public interface ExternalUserProvider {
    User fetchUserByUsername(String username);
}
