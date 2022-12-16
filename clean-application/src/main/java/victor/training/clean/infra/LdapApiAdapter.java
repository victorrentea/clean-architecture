package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.ILdapApiAdapter;

import java.util.List;

@RequiredArgsConstructor
@Adapter
public class LdapApiAdapter implements ILdapApiAdapter {
  private final LdapApi ldapApi;
  @Override
  public User fetchUserByUsername(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // ⚠️ data mapping mixed with biz logic
    User user = new User(dto.getUid(), dto.getWorkEmail(), fullName);
    return user;
  }
}
