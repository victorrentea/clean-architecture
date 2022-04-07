package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.entity.User;
import victor.training.clean.domain.service.IAdapter;

import java.util.List;

@RequiredArgsConstructor
@Component
public class Adapter implements IAdapter {
   // ascunde Dtouri si APiuri  externe
   private final LdapApi ldapApi;

   @Override
   public User retrieveUser(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }
      LdapUserDto ldapUser = list.get(0);

      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
      User user = new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());
      return user;
   }


}
