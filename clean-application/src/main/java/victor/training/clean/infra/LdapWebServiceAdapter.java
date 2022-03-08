package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.User;
import victor.training.clean.service.ExternalUserProvider;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LdapWebServiceAdapter implements ExternalUserProvider { // Adapter Design pattern
   private final LdapApi ldapApi;
//   @Value("${api.token}")
//   private String apiToken;

   @Override
   public User getOneUserByUsername(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase()/*, apiToken*/);
      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }
      return mapToEntity(list);
   }

   private User mapToEntity(List<LdapUserDto> list) {
      LdapUserDto dto = list.get(0);
      String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
      return new User(dto.getUid(), fullName, dto.getWorkEmail());
   }


}
