package victor.training.clean.infra;

import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserFetcher;

import java.util.List;
import java.util.Optional;

// 🧤 vs API, Lib, KafkaTemplate, WebClient, SF, SAP
@Adapter
public class LdapApiAdapter implements UserFetcher {
  private final LdapApi ldapApi;

  public LdapApiAdapter(LdapApi ldapApi) {
    this.ldapApi = ldapApi;
  }

  @Override
  public User fetchUser(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }

    // AL MEU!
    User user = new User(
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()),
        ldapUserDto.getUn());
    return user;
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }
}
