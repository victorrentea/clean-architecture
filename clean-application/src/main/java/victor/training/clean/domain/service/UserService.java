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

   public void importUserFromLdap(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }

      LdapUserDto ldapUser = list.get(0);

      deepDomainLogic(ldapUser);

   }

   private void deepDomainLogic(LdapUserDto ldapUser) {
      if (ldapUser.getWorkEmail()!=null) { // Replace with Optional<>
         log.debug("Send welcome email to  " + ldapUser.getWorkEmail());
      }

//      System.out.println(ldapUser.getWorkEmail().toLowerCase());

      log.debug("Insert user in my database: " + ldapUser.getUid()); // replace with username


      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase(); // not here. The way i se the user's name

      log.debug("More business logic with " + fullName + " of id " + ldapUser.getUid().toLowerCase()); // reject username=null
      innocentFix(ldapUser); // temporal coupling
   }

   private void innocentFix(LdapUserDto ldapUser) { // setters. mutable data.
      if (ldapUser.getUid() == null) {
         ldapUser.setUid("N/A"); // dirty fix should be made earlier
      }
   }

}
