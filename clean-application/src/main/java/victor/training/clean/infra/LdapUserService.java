package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.entity.User;
import victor.training.clean.service.ILdapUserService;
import victor.training.clean.service.LdapUserWebserviceClient;

import java.util.List;

//@DDD.Adapter
@RequiredArgsConstructor
public class LdapUserService implements ILdapUserService {
   private final LdapUserWebserviceClient wsClient;

   @Override
   public User findSingleUserByUsername(String username) {
      List<LdapUserDto> list = wsClient.search(username.toUpperCase(), null, null);
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }
      LdapUserDto dto = list.get(0);
      String fullName = dto.getfName() + " " + dto.getlName().toUpperCase();
      return new User(dto.getuId(), fullName, dto.getWorkEmail());
   }
}
