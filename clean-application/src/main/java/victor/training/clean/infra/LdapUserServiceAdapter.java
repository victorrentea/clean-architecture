package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.service.ExternaUserProvider;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LdapUserServiceAdapter implements ExternaUserProvider {
   private final LdapUserWebserviceClient wsClient;

   @Override
   public List<User> searchByUsername(String username) {
      return wsClient.search(username.toUpperCase(), null, null)
          .stream().map(this::convertUser)
          .collect(Collectors.toList());
   }

   private User convertUser(LdapUser ldapUser) {
      String fullName = extractFullName(ldapUser);
      return new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
   }

   private String extractFullName(LdapUser ldapUser) {
      return ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
   }


}
