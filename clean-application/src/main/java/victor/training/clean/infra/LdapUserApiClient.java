package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.ILdapUserApiClient;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
// not a UserRepository because a repo accesses MY PRIVATE DB
public class LdapUserApiClient implements ILdapUserApiClient { // "Adapter" design pattersn = a key of Anti-Corruption Layer
  private final LdapApi ldapApi;

  @Override
  public User fetchUserDetailsFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    // mapping logic ----
    LdapUserDto dto = dtoList.get(0);
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String username = dto.getUn();
    if (username.startsWith("s")) {// eg 's12051' is a system user
      username = "system"; // ⚠️ dirty hack
    }
//    String email = dto.getWorkEmail() != null ? dto.getWorkEmail() : "";
    User user = new User(username, fullName, Optional.ofNullable(dto.getWorkEmail()));
    // DO I allow in my app to receive a User with a null workemail?
    // NO=> throw here!
    // YES=> default to "" (A) = Null Object Pattern (GoF)
    // YES=> wrap it into an Optional<> (B) < 90% choose this
    return user;
  }

}
