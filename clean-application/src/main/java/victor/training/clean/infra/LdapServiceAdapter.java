package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.service.ILdapServiceAdapter;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LdapServiceAdapter implements ILdapServiceAdapter {
   private final LdapUserWebserviceClient wsClient;

   @Override
   public List<User> searchByUsername(String username) {
      return wsClient.search(username.toUpperCase(), null, null)
          .stream().map(this::fromDto)
          .collect(Collectors.toList());
   }

   private User fromDto(LdapUserDto dto) {
      String fullName = dto.getfName() + " " + dto.getlName().toUpperCase();
      return new User(dto.getuId(), fullName, dto.getWorkEmail());
   }


}
