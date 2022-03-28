package victor.training.clean.service;

import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LdapServiceClient {
   private final LdapApi ldapApi;

   LdapServiceClient(LdapApi ldapApi) {
      this.ldapApi = ldapApi;
   }

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
