package victor.training.clean.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@Slf4j
@Service
public class UserService {
   @Autowired
   private LdapApi ldapApi;

   public void importUserFromLdap(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }
      LdapUserDto ldapUser = list.get(0);
      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
      User user = new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());

      if (user.getWorkEmail().endsWith("@gov.com")) {
         log.debug("Send welcome email to Government staff: " + user.getWorkEmail().toLowerCase());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }


}
