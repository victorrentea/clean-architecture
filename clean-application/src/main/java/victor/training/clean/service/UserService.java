package victor.training.clean.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;

@Slf4j
@Service // DOMAIN SERVICE : core logic
// the most precious part of your app: MOST CLEAN CODE
public class UserService {
   @Autowired
   private LdapServiceClient ldapServiceClient;

   public void importUserFromLdap(String username) {
      User user = ldapServiceClient.loadUser(username);

      // biz logic from here on
      if (user.getWorkEmail() != null) {
         log.debug("Send welcome email to " + user.getWorkEmail());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}
