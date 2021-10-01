package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.infra.LdapUserDto;
import victor.training.clean.infra.LdapUserServiceAdapterInterface;
import victor.training.clean.infra.LdapUserWebserviceClient;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LdapUserServiceAdapter implements LdapUserServiceAdapterInterface {
   private final LdapUserWebserviceClient client;

   @Override
   public List<User> searchByUsername(String username) {
      return client.search(username.toUpperCase(), null, null)
          .stream()
          .map(this::fromDto)
          .collect(Collectors.toList());
   }

   private User fromDto(LdapUserDto dto) {
      String fullName = dto.getfName() + " " + dto.getlName().toUpperCase();
      return new User(dto.getuId(), fullName, dto.getWorkEmail());
   }


}
