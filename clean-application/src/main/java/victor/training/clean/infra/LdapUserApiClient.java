package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LdapUserApiClient implements victor.training.clean.domain.service.UserClient { // Adapter Design Pattern: convert between a baD api outside into a nice API inside.
  private final LdapApi ldapApi;

  @Override
  public User fetchUser(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
    return new User(ldapUserDto.getUn(), fullName, Optional.ofNullable(ldapUserDto.getWorkEmail()));
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }
}
