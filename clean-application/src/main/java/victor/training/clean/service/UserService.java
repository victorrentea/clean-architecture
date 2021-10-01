package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;
import victor.training.clean.infra.LdapUserDto;
import victor.training.clean.infra.LdapUserWebserviceClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final LdapUserServiceAdapter adapter;

	public void importUserFromLdap(String username) {
		List<User> list = adapter.searchByUsername(username);
		if (list.size() != 1) {
			throw new IllegalArgumentException("There is no single user matching username " + username);
		}
		User user = list.get(0);

		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}
		log.debug("Insert user in my database");
	}
// --- deasupra liniei e sufrageria
	///-----------
	// buda

}

