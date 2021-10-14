package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.entity.User;

import java.util.List;

@Slf4j
@Service // de descalti la ciorapi
@RequiredArgsConstructor
public class UserService {
	private final ILdapServiceAdapter adapter;

	public void importUserFromLdap(String username) {
		List<User> list = adapter.searchByUsername(username);
		if (list.size() != 1) {
			throw new IllegalArgumentException("There is no single user matching username " + username);
		}

		User user =  list.get(0);

		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}
		log.debug("Insert user in my database");
		log.debug("More business logic with " + user.getFullName());
	}
}
