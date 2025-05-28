package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

// "Adapter" design pattern acting here as an
// Anti-Corruption Layer (ACL)
@Slf4j
@RequiredArgsConstructor
@Service
// Architect (@thomas) PR says: bleah,
// why domain..NotificationService->infra..LdapBlaBla

// AGNOSTIC DOMAIN dream =
// domain should not CARE of the outside world
// when working in the domain you should not CARE of outside world
public class LdapUserApiAdapter implements victor.training.clean.domain.service.ILdapUserApiAdapter {
  private final LdapApi ldapApi;

  @Override
  public User fetchUser(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    normalize(ldapUserDto);

    return new User(
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()),
        ldapUserDto.getUn()
    );
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }
}
