package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.service.IAdapter;

import java.util.List;

// aka "Gateway"
@Component
@RequiredArgsConstructor
public class Adapter implements IAdapter {
   private final LdapApi ldapApi;

   @Override
   public User getUserFromLdap(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }
      LdapUserDto ldapUser = list.get(0);
      return toEntity(ldapUser);
   }

   private User toEntity(LdapUserDto ldapUser) {
      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
      return new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());
   }

}
