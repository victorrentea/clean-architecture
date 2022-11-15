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
@Service
public class UserService {
   private final LdapApi ldapApi;

   public void importUserFromLdap(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }

      LdapUserDto ldapUser = list.get(0);

      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase(); // not here. The way i se the user's name
      User user = new User(ldapUser.getUid()==null?"N/A": ldapUser.getUid(), ldapUser.getWorkEmail(), fullName);

      deepDomainLogic(user);

   }

   private void deepDomainLogic(User user) {
      if (user.getEmail().isPresent()) { // Replace with Optional<>
         log.debug("Send welcome email to  " + user.getEmail().get());
      }

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
