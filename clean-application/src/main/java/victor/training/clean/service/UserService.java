package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;

@Slf4j
@Service
// DOMAIN LOGIC
// ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN
// ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN
// ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN
// ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN
// ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN
// ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN
// ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN
// ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN ZEN
@RequiredArgsConstructor
public class UserService {
	private final ILdapUserService ldapUserService;

	public void importUserFromLdap(String username) {
		User user = ldapUserService.findSingleUserByUsername(username);

		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}

		log.debug("Insert user in my database");
		log.debug("More business logic with " + user.getFullName());
	}
}


