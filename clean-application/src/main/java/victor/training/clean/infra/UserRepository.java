package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepository {
  private final LdapApi ldapApi;

  public User search(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);
    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }
    // fetchin an AccesToken/apikey
    // retry
    // setting timeouts
    // calling 2+ APIs to collect all data needed
    LdapUserDto dto = dtoList.get(0);
    String username = normalizeUsername(dto.getUn());
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String workEmail = dto.getWorkEmail(); // may be null
    return new User(username, fullName, Optional.ofNullable(workEmail));
  }

  private String normalizeUsername(String username) {
    if (username != null && username.startsWith("s")) {
      return "system";
    }
    return username;
  }
}
