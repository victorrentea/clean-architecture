package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class LdapApiAdapter {
  private final LdapApi ldapApi;

  //  public LdapUserDto method() { // leak out of the infra back into domain // ILLEGAL
//
//  }
  public @NonNull User retrieveUser(String usernamePart) {
    // ⚠️ Scary, large external DTO FIXME only using a small set of properties
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);
    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);

    // ⚠️ Data mapping mixed with core logic FIXME pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }

    User user = new User(
        ldapUserDto.getUn(),
        fullName,
        ofNullable(ldapUserDto.getWorkEmail())
            .map(String::trim)
            .map(String::toLowerCase)
            .filter(String::isBlank)
    );
    return user;
  }
}
