package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LdapClientImpl {
  private final LdapApi ldapApi;

  public User search(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);
    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }
    LdapUserDto dto = dtoList.get(0);
    String username = normalizeUsername(dto.getUn());
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String workEmail = dto.getWorkEmail(); // may be null
    return new User(username, fullName, workEmail);
  }

  private String normalizeUsername(String username) {
    if (username != null && username.startsWith("s")) {
      return "system";
    }
    return username;
  }
}
