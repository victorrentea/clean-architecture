package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.ILdapApiAdapter;

import java.util.List;
import java.util.Optional;

//@Component
@Adapter
@RequiredArgsConstructor
public class LdapApiAdapter implements ILdapApiAdapter {
  private final LdapApi ldapApi;

  @Override
  public User loadUserFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    String username = dto.getUn();
    if (username.startsWith("s")) {// eg s12051 - a system user
      username= "system"; // ⚠️ dirty hack
    }
//    if (dto.getWorkEmail() == null) {
//      throw new IllegalArgumentException();
//    }
    return new User(
        username,
        dto.getFname() + " " + dto.getLname().toUpperCase(),
        Optional.ofNullable(dto.getWorkEmail()
        ));
  }
}
