package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

//class UserAdapter {
//class UserApiAdapter {
//class LdapUserApiAdapter {
@Service
@RequiredArgsConstructor
public class UserFetcher {
  private final victor.training.clean.infra.LdapApi ldapApi;

  public User fetchUser(String usernamePart) {
    victor.training.clean.infra.LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    return map(ldapUserDto);
  }

  private User map(victor.training.clean.infra.LdapUserDto ldapUserDto) {
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    normalize(ldapUserDto);

    User user = new User(
        ldapUserDto.getUn(),
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()
        ));
    return user;
  }

  private victor.training.clean.infra.LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<victor.training.clean.infra.LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

  private void normalize(victor.training.clean.infra.LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }

}
