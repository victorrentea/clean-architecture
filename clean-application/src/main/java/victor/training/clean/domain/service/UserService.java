package victor.training.clean.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.entity.User;
import victor.training.clean.infra.Adapter;

@Slf4j
@Service // holy domain logic
public class UserService {
   @Autowired
   private Adapter adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.retrieveUser(username);

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
