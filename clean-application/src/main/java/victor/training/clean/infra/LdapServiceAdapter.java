package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.entity.User;
import victor.training.clean.customer.service.ILdapServiceAdapter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class LdapServiceAdapter implements ILdapServiceAdapter {
   private final LdapUserWebserviceClient wsClient;

   @Override
   public List<User> searchByUsername(String username) {
      return wsClient.search(username.toUpperCase(), null, null)
          .stream().map(this::toEntity)
          .collect(toList());
   }

   private User toEntity(LdapUserDto ldapUser) {
      String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
      return new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
   }

}
