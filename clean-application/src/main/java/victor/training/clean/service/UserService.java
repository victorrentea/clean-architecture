package victor.training.clean.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;

@Slf4j
@Service
// presupunem ca in aceasta clasa vrem sa implementam logica centrala a app noastre : acea parte din requ care ne da fiori.
// Domain Logic = partea dintr-o app pe care nu ai cum s-o copiezi in alta.
public class UserService {
	@Autowired
	private LdapServiceAdapter ldapServiceAdapter;

	public void importUserFromLdap(String username) {
		User user = ldapServiceAdapter.searchOneByUsername(username);

		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}
		log.debug("Insert user in my database");
		log.debug("More business logic with " + user.getFullName());
	}


}
