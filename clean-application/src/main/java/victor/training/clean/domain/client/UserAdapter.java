package victor.training.clean.domain.client;

import victor.training.clean.domain.model.User;

public interface UserAdapter {

    User getUserByUsername(String targetUsername);

}
