package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.entity.User;
import victor.training.clean.customer.service.ExternalUserProvider;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LdapUserServiceAdapter implements ExternalUserProvider {
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
      if (ldapUser.getfName() == null && ldapUser.getlName() == null) {
         return "anonymous";
      }
      return ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
   }


}
