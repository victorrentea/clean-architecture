package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserFetcher;

import java.util.List;
import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class LdapUserFetcher implements UserFetcher {
  private final LdapApi ldapApi;

  @Override
  public User fetchUser(String usernamePart) {
    List<LdapUserDto> results = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);
    if (results.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + results);
    }
    LdapUserDto dto = results.get(0);
    return new User(
        normalizeUsername(dto.getUn()),
        dto.getFname() + " " + dto.getLname().toUpperCase(),
        Optional.ofNullable(dto.getWorkEmail()).map(String::toLowerCase));
  }

  private String normalizeUsername(String username) {
    return username.startsWith("s") ? "system" : username;
  }
}
