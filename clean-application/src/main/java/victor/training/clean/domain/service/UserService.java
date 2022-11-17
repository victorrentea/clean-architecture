package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service // domain logic
public class UserService {
   private final LdapServiceAdapter adapter;

   // assume this is domain core lopgic
   public void importUserFromLdap(String username) {
      User user = adapter.fetchByUsername(username);

      //smaller object more tailored to my need
      if (user.getEmail().isPresent()) {
         log.debug("Send welcome email to  " + user.getEmail());
      }
      //      ldapUser.getEmail().toLowerCase(); // NPE : solution: Optional<> getters

      //      ldapUser.setUid("N/A"); // OMG mutable!

      //      ldapUser.logic();// NOT possible because ldapUser is generated from an OpenAPI

      log.debug("Insert user in my database: " + user.getUsername()); // rename "username"

      // my domain only needs fullName not fName and lName
      log.debug("More business logic with " + user.getFullName() + " of id " + user.getUsername().toLowerCase());

   }


}
