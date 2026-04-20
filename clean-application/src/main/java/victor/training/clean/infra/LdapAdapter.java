package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.repo.UserDirectory;

import java.util.List;
import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class LdapAdapter implements UserDirectory {
  private final LdapApi ldapApi;

  public User findByUsernamePart(String usernamePart) {
    List<LdapUserDto> results = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);
    if (results.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + results);
    }
    LdapUserDto dto = results.get(0);
    String username = dto.getUn().startsWith("s") ? "system" : dto.getUn();
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    return new User(username, fullName, Optional.ofNullable(dto.getWorkEmail()));
  }
}

// reasons for the interface
// - allows to PROVE (multi-modules/archUnit) that domain KNOWN NOTHING about infra : prevent infra leak
// - Plug in a new implementation tomorrow w/o changing the 'domain'🤞🤞🦄🦄
// - C# can inject proxies of interfaces now (Java can for classes too)⭐️⭐️⭐️ eg timing a method, logging
// - Domain Test: can use C# mocks, ⭐️⭐️⭐️ are clean of LDAP💩💩💩 (no 'fName' in domain tests)