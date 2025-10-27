package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;

import java.util.List;

//public class FetchUser {
//public class UserFetcher {
@Component
@RequiredArgsConstructor
@Slf4j // ...Factory, ...Facade, ...Service, ...Repository
public class LdapApiAdapter {
  private final LdapApi ldapApi;

  public User fetchUserByName(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    normalize(ldapUserDto);
    User user = new User(ldapUserDto.getUn(),
        fullName,
        ldapUserDto.getWorkEmail());
    return user;
  }

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) { // eg s12315->system
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }
}
