package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;
import victor.training.clean.service.ExternalUserProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LdapApiAdapter implements ExternalUserProvider {
   private final LdapApi ldapApi;

   @Override
   public User findOneUserByUsername(String username) {
      List<LdapUserDto> list = searchByUsername(username);
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }

      LdapUserDto ldapUser = list.get(0);
      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();

      return new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());
   }

   private List<LdapUserDto> searchByUsername(String username) {
      return ldapApi.searchUsingGET(username.toUpperCase(), null, null);
   }
}