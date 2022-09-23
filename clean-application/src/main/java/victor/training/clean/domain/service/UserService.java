package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service // ZEN DOMAIN LOGIOC> PACE. ARMONIE. VERDEATA
public class UserService {
   private final ILdapApiAdapter adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.fetchUserByUsername(username);

      deepDomainLogic(user);

   }



   private void deepDomainLogic(User user) {
      if (user.hasWorkEmail()) {
         log.debug("Send welcome email to " + user.getWorkEmail());
      }

      log.debug("Insert user in my database");

      log.debug("More business logic with " + user.getCorporateName() + " of id " + user.getUsername().toLowerCase());
   }

}
