package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.entity.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service // DOmain Service... angels should sing here. peace. harmony. ying and yang. ZEN garden.
public class UserService {
   private final LdapUserDoor ldapUserDoor;

   public void importUserFromLdap(String username) {
      User user = ldapUserDoor.retrieveByUsername(username);


      if (user.hasWorkEmail()) {
         log.debug("Send welcome email to " + user.getWorkEmail());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}

