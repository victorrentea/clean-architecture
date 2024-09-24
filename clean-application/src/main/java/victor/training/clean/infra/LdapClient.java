package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserFetcher;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
// Adapter design pattern
public class LdapClient implements UserFetcher {
  private final LdapApi ldapApi;

//  public LdapUserDto lifeIsHard(String usernamePart) {
//    return fetchUserFromLdap(usernamePart);
//  }

  @Override
  public User fetchUserByUsername(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);
    User user = mapToMyDomain(ldapUserDto);
    return user;
  }

  private static User mapToMyDomain(LdapUserDto ldapUserDto) {
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
    User user = new User( // my own domain model!! 🎉
        ldapUserDto.getUn(),
        fullName,
        Optional.of(ldapUserDto.getWorkEmail()
        ));
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
