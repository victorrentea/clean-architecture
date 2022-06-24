package victor.training.clean.domain.insurance.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.insurance.entity.User;

@Slf4j
@Service // holy domain logic
public class UserService {
   @Autowired
   private ExternalUserProvider externalUserProvider;

   public void importUserFromLdap(String username) {
      User user = externalUserProvider.retrieveUser(username);
//      LdapUserDto doesNotCompile; // innaccessible

      if (user.getWorkEmail().endsWith("@gov.com")) {
         log.debug("Send welcome email to Government staff: " + user.getWorkEmail().toLowerCase());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}
