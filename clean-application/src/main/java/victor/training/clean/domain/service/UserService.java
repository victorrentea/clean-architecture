package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
   private final ExternalUserProvider externalUserProvider;

   public void importUserFrom(String username) {
      User user = externalUserProvider.retrieveUserById(username);
      // DO NOT DO defensive programming in our core logic  < fear driven development
      if (user.getEmail().isPresent()) {
         log.debug("Send welcome email to  " + user.getEmail().get());
      }

      log.debug("Insert user in my database: " + user.getUsername());

      log.debug("More business logic with " + user.getFullName() + " of id " + user.getUsername().toLowerCase());

      // then, in multiple places:
      user.getEmailContact().ifPresent(this::sendMailTo);
      user.getEmailContact().ifPresent(this::sendMailTo);
      user.getEmailContact().ifPresent(this::sendMailTo);
      user.getEmailContact().ifPresent(this::sendMailTo);

   }


   private void sendMailTo(String emailContact) {
      System.out.println("Contact: " + emailContact);
      //..implem left out
   }

}
