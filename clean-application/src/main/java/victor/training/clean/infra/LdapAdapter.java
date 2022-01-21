package victor.training.clean.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.service.ILdapAdapter;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LdapAdapter implements ILdapAdapter {
   @Autowired
   public LdapUserWebserviceClient wsClient;

   public User fromDto(LdapUserDto ldapUser) {
      String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
      return new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
   }

   @Override
   public List<User> searchByUsername(String username) {
      return wsClient.search(username.toUpperCase(), null, null)
          .stream().map(this::fromDto).collect(Collectors.toList());
   }
}