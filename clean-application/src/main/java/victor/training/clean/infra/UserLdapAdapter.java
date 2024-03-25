package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
// I call an external ugly API,
// but I expose a NICE api to my caller (the rest of my logic)
// what desing pattern is this?
public class UserLdapAdapter implements UserService {
    private final LdapApi ldapApi;

  public User fetchUser(String username) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(username.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + username + "' returned too many results: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);

    String u = ldapUserDto.getUn().startsWith("s") ? "system" : ldapUserDto.getUn();
    return new User(
        u,
        Optional.ofNullable(ldapUserDto.getWorkEmail()),
        ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase());
  }

}
