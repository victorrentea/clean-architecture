package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

// Mapper only transforms data
// Adapter (Design Pattern) = an intermediary protecting your from what you want to use (external API)
//     also includes mapping (sometimes can be extracted in a seaparate "Mapper" class)
@RequiredArgsConstructor
@Component
public class LdapClient {
  private final LdapApi ldapApi;

  // under this line, behold: SHIT !!! ------------------------------

  public User loadUserFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }
    User user = convert(dtoList.get(0));
    return user;
  }

  private static User convert(LdapUserDto userDto) {
    String fullName = userDto.getFname() + " " + userDto.getLname().toUpperCase();
    String username = userDto.getUn();
    if (username.startsWith("s")) username = "system";
    return new User(username, fullName, userDto.getWorkEmail());
  }

}
