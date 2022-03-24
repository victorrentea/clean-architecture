package victor.training.clean.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.user.entity.User;

@Slf4j
@Service // place for my core logic. peace. harmony. ZEN. Ying and Yang.
public class UserService {
   private final ExternalUserProvider externalUserProvider;

   public UserService(ExternalUserProvider externalUserProvider) {
      this.externalUserProvider = externalUserProvider;
   }

   public void importUserFromLdap(String username) {
      User user = externalUserProvider.loadUser(username);

      if (user.hasEmail()) {
         log.debug("Send welcome email to " + user.getWorkEmail().orElse(""));
      }

      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}
