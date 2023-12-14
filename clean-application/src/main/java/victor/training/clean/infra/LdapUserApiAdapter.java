package victor.training.clean.infra;

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


  // infra gargabe
  public User fetchUserDetailsFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    String un = dtoList.get(0).getUn();
    if (un.startsWith("s")) {// eg 's12051' is a system user
      un="system"; // ⚠️ dirty hack 21:00 Fri
    }

    String fullName = dtoList.get(0).getFname() + " " + dtoList.get(0).getLname();
    User user = new User(un,
        fullName,
        Optional.ofNullable(dtoList.get(0).getWorkEmail()));
    return  user;
  }
}
