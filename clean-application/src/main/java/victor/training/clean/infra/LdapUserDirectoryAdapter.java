package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.port.UserDirectory;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LdapUserDirectoryAdapter implements UserDirectory {
  private final LdapApi ldapApi;

  private static String toUpper(String s) {
    return s == null ? null : s.toUpperCase();
  }

  private static String nvl(String s) {
    return s == null ? "" : s;
  }

  private static String normalizeUsername(String un) {
    if (un.startsWith("s")) {
      return "system";
    }
    return un;
  }

  private static String buildFullName(String fname, String lname) {
    String fullName = (fname + " " + (lname == null ? "" : lname.toUpperCase())).trim();
    return fullName;
  }

  private static String normalizeEmail(String email) {
    if (email == null) return null;
    String trimmed = email.trim();
    if (trimmed.isBlank()) return null;
    return trimmed.toLowerCase();
  }

  @Override
  public User findSingleByUsernamePart(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(toUpper(usernamePart), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    String username = normalizeUsername(nvl(dto.getUn()));
    String fullName = buildFullName(nvl(dto.getFname()), nvl(dto.getLname()));
    String normalizedEmail = normalizeEmail(dto.getWorkEmail());
    Optional<String> workEmail = Optional.ofNullable(normalizedEmail);

    return new User(username, fullName, workEmail);
  }
}
