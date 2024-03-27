package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.IUserFetcher;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFetcher implements IUserFetcher {
  private final LdapApi ldapApi;

  @Override
  public User fetch(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);
    String username = ldapUserDto.getUn();
    if (username.startsWith("s")) {
      username = "system"; // ⚠️ dirty hack: replace any system user with 'system'
    }
    User user = new User(username, Optional.ofNullable(ldapUserDto.getWorkEmail()),
        ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase());
    return user;
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

}
