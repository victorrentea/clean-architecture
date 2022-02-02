package victor.training.clean.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.infra.LdapUserDto;
import victor.training.clean.infra.LdapUserWebserviceClient;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LdapServiceAdapter {
   @Autowired
   public LdapUserWebserviceClient wsClient;

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