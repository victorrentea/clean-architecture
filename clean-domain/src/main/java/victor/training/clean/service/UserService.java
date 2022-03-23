package victor.training.clean.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;

@Slf4j
@Service // place for my core logic. peace. harmony. ZEN. Ying and Yang.
public class UserService {
   @Autowired
   private IUserService adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.loadUser(username);

      if (user.hasEmail()) {
         log.debug("Send welcome email to " + user.getWorkEmail().orElse(""));
      }

      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}
