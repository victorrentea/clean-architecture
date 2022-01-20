package victor.training.clean.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;
import victor.training.clean.infra.LdapUser;

import java.util.List;

@Slf4j
@Service
public class UserService {
	private final LdapClientAdapter ldapClientAdapter = new LdapClientAdapter();

	public void importUserFromLdap(String username) {
		List<User> list = ldapClientAdapter.searchByUsername(username);
		if (list.size() != 1) {
			throw new IllegalArgumentException("There is no single user matching username " + username);
		}
		User user = list.get(0);

		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}
		log.debug("Insert user in my database");
		log.debug("More business logic with " + user.getFullName());
	}

	private List<User> searchByUsername(String username) {
		return ldapClientAdapter.searchByUsername(username);
	}

	private User fromDto(LdapUser ldapUser) {
		return ldapClientAdapter.fromDto(ldapUser);
	}


}
