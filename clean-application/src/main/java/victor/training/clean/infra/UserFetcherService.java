package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.IUserFetcherService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFetcherService implements IUserFetcherService {
  private final LdapApi ldapApi;

  // SHIT in DOMAIN package
  @Override
  public User fetchUser(Customer customer, String usernamePart) {
    LdapUserDto ldapUserDto = fetchUser(usernamePart);
    String username = ldapUserDto.getUn();
    if (username.startsWith("s")) {
      username = "system"; // ⚠️ dirty hack: replace any system user with 'system'
    }
    User user = new User(username,
        ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase(),
        Optional.ofNullable(customer.getEmail()));
    return user;
  }

  private LdapUserDto fetchUser(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }
}
