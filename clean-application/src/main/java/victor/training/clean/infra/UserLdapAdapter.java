package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserPort;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserLdapAdapter implements UserPort {
  private final LdapApi ldapApi;
  @Override
  public User fetchUser(String userId) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);

    String username = ldapUserDto.getUn();
    if (username.startsWith("s")) {// eg 's12051' is a system user
      username = "system"; // ⚠️ dirty hack
    }

    User user = new User(username,
        ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase(),
        ldapUserDto.getWorkEmail());
    return user;
  }
}
