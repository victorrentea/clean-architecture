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
@Service // domain service
public class UserService {
   private final LdapApi ldapApi;
   private final ClientApiAdapter adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.fetchUserByUsername(username);

      if (user.getEmail().isPresent()) {
         log.debug("Send welcome email to  " + user.getEmail().get());
      }

      log.debug("Insert user in my database: " + user.getUsername()); // bad names : "username"

      // evil unexpected temporal coupling: hack should happen before uid.toLowerCase()
//      innocentHack(user);
      log.debug("More business logic with " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // NUll !!! OMG

      // then, in multiple places:
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
   }

//   private void innocentHack(User user) {
//      if (user.getUid() == null) {
//         user.setuseUid("anonymous"); // setters ??!! yuuu 🤢
//      }
//   }

   private void sendMailTo(String emailContact) {
      System.out.println("Contact: " + emailContact);
      //..implem left out
   }

}
