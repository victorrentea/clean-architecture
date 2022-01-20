package victor.training.clean.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;
import victor.training.clean.infra.LdapUser;
import victor.training.clean.infra.LdapUserWebserviceClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LdapClientAdapter {
   @Autowired
   private LdapUserWebserviceClient wsClient;

   public List<User> searchByUsername(String username) {
      return wsClient.search(username.toUpperCase(), null, null)
          .stream().map(this::fromDto)
          .collect(Collectors.toList());
   }

   private User fromDto(LdapUser ldapUser) {
      String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
      return new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
   }
}