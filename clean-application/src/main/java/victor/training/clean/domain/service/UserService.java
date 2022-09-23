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
      if (ldapUser.getWorkEmail()!=null) {
         log.debug("Send welcome email to " + ldapUser.getWorkEmail());
      }

      log.debug("Insert user in my database");

      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
      log.debug("More business logic with " + fullName + " of id " + ldapUser.getUid().toLowerCase());
   }

}
