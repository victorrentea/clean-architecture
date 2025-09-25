package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.Adapter;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@Component // Adapter design pattern: shielding your domain complexity against the external corruption
// = Anti-Corruption Layer
//@Adapter
@Slf4j
@RequiredArgsConstructor
public class LdapClient {
  private final LdapApi ldapApi;

  public User fetchUser(String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);
    normalize(ldapUserDto);

    // ⚠️ Data mapping mixed with core logic TODO pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    return new User(
        ldapUserDto.getUn(),
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail())
    );
  }

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }
}
