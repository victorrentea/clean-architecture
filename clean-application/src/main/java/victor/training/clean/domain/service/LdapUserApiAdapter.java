package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LdapUserApiAdapter {
  private final LdapApi ldapApi;

  public User retrieveUser(String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    User user = map(ldapUserDto);
    return user;
  }

  private User map(LdapUserDto ldapUserDto) {
    // ⚠️ Data mapping mixed with core logic TODO pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    normalize(ldapUserDto); // NU E FUNCTIE PURA(=da acelasi rez fara sa modifice chestii)!

    User user = new User(fullName,
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

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }

}
