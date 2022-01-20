package victor.training.clean.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;
import victor.training.clean.service.ILdapClientAdapter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LdapClientAdapter implements ILdapClientAdapter {
   @Autowired
   private LdapUserWebserviceClient wsClient;

   @Override
   public List<User> searchByUsername(String username) {
      return wsClient.search(username.toUpperCase(), null, null)
          .stream()
          .map(this::fromDto)
          .collect(Collectors.toList());
   }

   private User fromDto(LdapUser ldapUser) {
      String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
      return new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
   }
}