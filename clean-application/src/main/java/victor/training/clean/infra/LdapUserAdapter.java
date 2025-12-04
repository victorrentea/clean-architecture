package victor.training.clean.infra;

import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
public class LdapUserAdapter implements UserRepository {
  private final LdapApi ldapApi;

  public LdapUserAdapter(LdapApi ldapApi) {
    this.ldapApi = ldapApi;
  }

  private static String buildFullName(LdapUserDto dto) {
    String fname = dto.getFname() == null ? "" : dto.getFname();
    String lname = dto.getLname() == null ? "" : dto.getLname().toUpperCase();
    String sep = (fname.isEmpty() || lname.isEmpty()) ? "" : " ";
    return fname + sep + lname;
  }

  private static String normalizeUsername(String un) {
    if (un == null) return null;
    if (un.startsWith("s")) {
      return "system"; // replace any system user with 'system'
    }
    return un;
  }

  @Override
  public User findSingleUserByUsernamePart(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);
    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }
    LdapUserDto dto = dtoList.get(0);

    String username = normalizeUsername(dto.getUn());
    String fullName = buildFullName(dto);
    String workEmail = dto.getWorkEmail();

    return new User(username, fullName, Optional.ofNullable(workEmail));
  }
}
