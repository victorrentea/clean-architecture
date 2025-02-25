package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

// Adapter design pattern, in a Hexagonal arch is a OutputPort
@Component
@RequiredArgsConstructor
public class UserProvider {
  private final LdapApi ldapApi;

  public User fetchUserByUsername(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }

    return new User(ldapUserDto.getUn(),
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()));
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }
}
