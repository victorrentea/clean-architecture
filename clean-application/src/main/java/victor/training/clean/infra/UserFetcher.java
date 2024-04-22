package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.model.Username;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserFetcher implements victor.training.clean.domain.service.UserFetcher {
  private final LdapApi ldapApi;

  @Override
  public User fetch(String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    // is there any relevant domain difference between "" and null email ? NO: never see an "" in your core logic.
    if (ldapUserDto.getWorkEmail()!=null && ldapUserDto.getWorkEmail().equals("")) {
      ldapUserDto.setWorkEmail(null);
    }

    String username = ldapUserDto.getUn();
    if ("".equals(username)) {
      username = null;
    } else if (username != null) {
      if (username.startsWith("s")) {
        username="system";
      }
    }
    User user = new User(
        new Username(username),
        ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase(),
        Optional.ofNullable(ldapUserDto.getWorkEmail()));
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
