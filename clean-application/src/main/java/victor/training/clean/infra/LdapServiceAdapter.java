package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.MyException;
import victor.training.clean.MyException.ErrorCode;
import victor.training.clean.entity.User;
import victor.training.clean.service.ExternalUserProvider;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LdapServiceAdapter implements ExternalUserProvider {
   private final LdapUserWebserviceClient wsClient;

   @Override
   public List<User> searchByUsername(String username) {
      try {
         return wsClient.search(username.toUpperCase(), null, null)
             .stream().map(this::convert).collect(Collectors.toList());
      } catch (Exception e) {
         throw new MyException(ErrorCode.CUSTOMER_NAME_TOO_SHORT);
      }
   }
   public List<LdapUser> other(String username) {
      return Collections.emptyList();
   }

   private User convert(LdapUser ldapUser) {
      String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
      return new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
   }

}
