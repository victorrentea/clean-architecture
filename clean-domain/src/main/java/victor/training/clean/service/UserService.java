package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
// Domain Service
public class UserService {
	private final ExternalUserService externalUserService;

	public void importUserFromLdap(String username) {
		List<User> list = externalUserService.searchByUsername(username);
		if (list.size() != 1) {
			throw new IllegalArgumentException("There is no single user matching username " + username);
		}
		User user = list.get(0);

		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}
		log.debug("Insert user in my database");
	}

}
