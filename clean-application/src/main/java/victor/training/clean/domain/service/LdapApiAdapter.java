package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LdapApiAdapter {
  private final LdapApi ldapApi;

  public  User fetchUser(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);

    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    return new User(ldapUserDto.getUn(),fullName, Optional.ofNullable(ldapUserDto.getWorkEmail()));
  }
}
