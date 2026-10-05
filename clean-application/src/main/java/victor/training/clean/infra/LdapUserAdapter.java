package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserProvider;

import java.util.List;
import java.util.Optional;

/**
 * Adapter: the only class in the app that talks to LdapApi. It absorbs the remote call,
 * the "exactly one result" check and the DTO mapping, so the domain sees a plain {@link User}.
 */
@Adapter
@RequiredArgsConstructor
public class LdapUserAdapter implements UserProvider {
  private final LdapApi ldapApi;

  @Override
  public User findByUsernamePart(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);
    return new User(
        dto.getUn().startsWith("s") ? "system" : dto.getUn(), // ⚠️ dirty hack, but now contained
        dto.getFname() + " " + dto.getLname().toUpperCase(),
        Optional.ofNullable(dto.getWorkEmail()).map(String::toLowerCase));
  }
}
