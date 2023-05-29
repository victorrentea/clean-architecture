package victor.training.clean.domain;

import victor.training.clean.domain.model.User;

public interface LdapClientInterface {
	User getUserByName(String name);
}
