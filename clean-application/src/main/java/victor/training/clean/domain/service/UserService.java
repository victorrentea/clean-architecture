package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service // zen garden . peace. harmony, business logic.
public class UserService {
   private final LdapAdapter ldapAdapter;

   public void importUserFromLdap(String username) {
      User user = ldapAdapter.retrieveByUsername(username);
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
