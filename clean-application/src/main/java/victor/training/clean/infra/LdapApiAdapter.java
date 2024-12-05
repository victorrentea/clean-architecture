package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;


@Slf4j
@Adapter
@RequiredArgsConstructor
public class LdapApiAdapter implements victor.training.clean.domain.service.UserFetcher { // call to DB=1ms vs call to APIs = 10ms
  private final LdapApi ldapApi;

  @Override
  public User fetchUserByUsername(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);
    var fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    normalize(ldapUserDto);
    return new User(ldapUserDto.getUn(),
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()));
  }

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }


}
