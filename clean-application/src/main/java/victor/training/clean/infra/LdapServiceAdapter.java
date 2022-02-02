package victor.training.clean.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.service.ILdapServiceAdapter;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LdapServiceAdapter implements ILdapServiceAdapter {
   @Autowired
   public LdapUserWebserviceClient wsClient;

   @Override
   public User searchOneByUsername(String username) {
      List<User> list = wsClient.search(username.toUpperCase(), null, null)
          .stream()
          .map(this::fromDto)
          .collect(Collectors.toList());
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }
      return list.get(0);
   }

   private User fromDto(LdapUserDto dto) {
      String fullName = dto.getfName() + " " + dto.getlName().toUpperCase();
      return new User(dto.getuId(), fullName, dto.getWorkEmail());
   }
}