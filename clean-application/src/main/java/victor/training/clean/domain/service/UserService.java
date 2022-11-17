package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
   private final LdapApi ldapApi;

   // assume this is domain core lopgic
   public void importUserFromLdap(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }

      LdapUserDto dto = list.get(0);

      String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
      User user = new User(dto.getUid(), dto.getWorkEmail(), fullName);

      deepDomainLogic(user);

   }

   private void deepDomainLogic(User ldapUser) { //smaller object more tailored to my need
      if (ldapUser.getEmail().isPresent()) {
         log.debug("Send welcome email to  " + ldapUser.getEmail());
      }
//      ldapUser.getEmail().toLowerCase(); // NPE : solution: Optional<> getters

//      ldapUser.setUid("N/A"); // OMG mutable!

//      ldapUser.logic();// NOT possible because ldapUser is generated from an OpenAPI

      log.debug("Insert user in my database: " + ldapUser.getUsername()); // rename "username"

      // my domain only needs fullName not fName and lName
      log.debug("More business logic with " + ldapUser.getFullName() + " of id " + ldapUser.getUsername().toLowerCase());
   }

}
