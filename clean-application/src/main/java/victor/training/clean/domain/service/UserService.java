package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
   private final ExternalUserProvider adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.fetchUserByUsername(username);
      // an object enters my domain logic with too much data
      if (user.getEmail().isPresent()) { // null in fields. NO i want optional !
         log.debug("Send welcome email to  " + user.getEmail().get()); // "work" is redundant
      }

      log.debug("Insert user in my database: " + user.getUsername()); // bad name "username"

      log.debug("More business logic with " + user.getFullName() + " of id " + user.getUsername().toLowerCase());

      // then, in multiple places:
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
   }


   private void sendMailTo(String emailContact) {
      System.out.println("Contact: " + emailContact);
      //..implem left out
   }

}
