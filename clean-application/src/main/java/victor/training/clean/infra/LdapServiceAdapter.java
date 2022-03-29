package victor.training.clean.infra;

import org.springframework.stereotype.Component;
import victor.training.clean.user.entity.User;
import victor.training.clean.user.service.LdapService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LdapServiceAdapter implements LdapService { // "Ports-and-Adapters"
   private final LdapApi ldapApi;

   LdapServiceAdapter(LdapApi ldapApi) {
      this.ldapApi = ldapApi;
   }

   @Override
   public User loadUser(String username) {
      List<User> list = ldapApi.searchUsingGET(null, null, username.toUpperCase())
          .stream().map(this::mapToDomain)
          .collect(Collectors.toList());
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }
      return list.get(0);
   }

   private User mapToDomain(LdapUserDto ldapUser) {
      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
      return new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());
   }


}
