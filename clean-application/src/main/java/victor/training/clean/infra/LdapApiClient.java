package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

//AntiCourruptionLayer defending the Domain from the outside world
@Slf4j
@Service
@RequiredArgsConstructor
public class LdapApiClient implements victor.training.clean.domain.service.UserApiClient // adapter pattern
{
  private final LdapApi ldapApi;
 // Dirty
 @Override
 public User fetchByUsername(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    normalize(ldapUserDto);

    return new User(
        ldapUserDto.getUn(),
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()));
  }

  // Dirty
  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

  // Dirty
  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }



}
