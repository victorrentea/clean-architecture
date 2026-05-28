package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class LdapUserAdapter {
  private final LdapApi ldapApi;

  public User findUserByUsernamePart(String usernamePart) {
    List<LdapUserDto> results = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);
    if (results.size() != 1) {
      throw new IllegalArgumentException(
          "Search for username='" + usernamePart + "' did not return a single result: " + results);
    }
    LdapUserDto dto = results.get(0);
    return new User(
        dto.getFname(),
        dto.getLname(),
        normalizeUsername(dto.getUn()),
        Optional.ofNullable(dto.getWorkEmail())
    );
  }

  private static String normalizeUsername(String un) {
    return un.startsWith("s") ? "system" : un;
  }
}
