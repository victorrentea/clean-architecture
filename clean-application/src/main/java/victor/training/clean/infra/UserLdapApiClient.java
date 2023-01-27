package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;



// Adapter pattern din GoF
@RequiredArgsConstructor
@Adapter
// PR reject: ce cauta asta in acelasi pachet cu domain logic/ poti mai mult. de la tine vroiam mai mult
public class UserLdapApiClient {
  private final LdapApi ldapApi;

  public User fetchUser(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);
    String un = dto.getUid() == null ? "anonymous" : dto.getUid().toUpperCase();
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // ⚠️ data mapping mixed with biz logic

    User user = new User(un, fullName, dto.getWorkEmail());
    return user;
  }
}
