package victor.training.clean.infra;

import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.service.IUserService;

import java.util.List;

@Component
public class LdapUserServiceAdapter implements IUserService {
   private final LdapApi ldapApi;

   public LdapUserServiceAdapter(LdapApi ldapApi) {
      this.ldapApi = ldapApi;
   }

   @Override
   public User loadUser(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }
      LdapUserDto ldapUser = list.get(0);

      String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
      return new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());
   }
}
