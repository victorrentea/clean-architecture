package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserFetcher;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LdapService implements UserFetcher {
  private final LdapApi ldapApi;

//  @Cacheable("user")
//  @MyCacheable("user")
  @Override
  public User fetchUser(String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);
    normalize(ldapUserDto);
    User user = new User(ldapUserDto.getUn(),
        ldapUserDto.getFname() + " " + ldapUserDto.getLname(),
        Optional.ofNullable(ldapUserDto.getWorkEmail()));
    return user;
  }
  private void normalize(LdapUserDto dto) {
    if (dto.getUn().startsWith("s")) {
      dto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }

   private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }
}
