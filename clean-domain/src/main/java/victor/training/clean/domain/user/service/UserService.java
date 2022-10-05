package victor.training.clean.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.user.model.User;

@RequiredArgsConstructor
@Slf4j
@Service // zen garden . peace. harmony, business logic.
public class UserService {
   private final ExternalUserProvider externalUserProvider;

   public void importUserFromLdap(String username) {
      User user = externalUserProvider.retrieveByUsername(username);
      deepDomainLogic(user);

   }



   private void deepDomainLogic(User user) {
      if (user.hasWorkEmail()) {
         log.debug("Send welcome email to  " + user.getWorkEmail());
      }

      log.debug("Insert user in my database: " + user.getUsername());

      log.debug("More business logic with " + user.getFullName() + " of id " + user.getUsername().toLowerCase());
   }

}
