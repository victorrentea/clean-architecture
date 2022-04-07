package victor.training.clean.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.entity.User;

@Slf4j

// STA in DOMAIN. ZEN. PACE. ARMONIE.
// Codu fain, de pus la CV cum arata
// Clean Code extrem, teste...
@Service
public class UserService {
   @Autowired
   private IAdapter adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.retrieveUser(username);

      if (user.hasWorkEmail()) {
         log.debug("Send welcome email to " + user.getWorkEmail());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}
