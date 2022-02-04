package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;

@RequiredArgsConstructor
@Slf4j
@Service // DOmain Service : sacred grounds
public class UserService {
	private final ExternalUserProvider userProvider;

	public void importUserFromLdap(String username) {
		User user = userProvider.findOneUserByUsername(username);

		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}
		log.debug("Insert user in my database");
		log.debug("More business logic with " + user.getFullName());
	}


}
