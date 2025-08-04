package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
//@Adapter
@Component
public class LdapAdapter implements victor.training.clean.domain.service.UserInventoryPort {
  // infra 💩 of outside world (out adapter) = External Corruption
  private final LdapApi ldapApi;

  @Override
  public User fetchUser(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);
    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }
    LdapUserDto ldapUserDto = dtoList.get(0);
    normalize(ldapUserDto);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    return new User(
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()),
        ldapUserDto.getUn());
  }

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }

}
