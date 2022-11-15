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
      User user = adapter.fetchByUsername(username);

      if (user.getEmail().isPresent()) { // Replace with Optional<>
         log.debug("Send welcome email to  " + user.getEmail().get());
      }
//      LdapUserDto wrong; // not compile

      //      System.out.println(user.getEmail().toLowerCase());// does not compile!

      // imagine complex logic
      // imagine complex logic
      // imagine complex logic
      // imagine complex logic
      log.debug("Insert user in my database: " + user.getUsername()); // replace with username


      log.debug("More business logic with " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // reject username=null
      //      innocentFix(user); // temporal coupling

   }


   //   private void innocentFix(LdapUserDto ldapUser) { // setters. mutable data.
//      if (ldapUser.getUid() == null) {
//         ldapUser.setUid("N/A"); // dirty fix should be made earlier
//      }
//   }

}
