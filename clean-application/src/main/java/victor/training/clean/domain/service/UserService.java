package victor.training.clean.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.entity.User;

@Slf4j
@Service // DOMAIN SERVICE : core logic
// the most precious part of your app: MOST CLEAN CODE
public class UserService {
   @Autowired
   private LdapService ldapService;

   public void importUserFromLdap(String username) {
      User user = ldapService.loadUser(username);

      // biz logic from here on
      if (user.hasEmail()) {
         log.debug("Send welcome email to " + user.getWorkEmail().get());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}
