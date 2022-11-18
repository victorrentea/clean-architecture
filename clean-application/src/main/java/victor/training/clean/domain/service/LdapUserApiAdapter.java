package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@Adapter
@RequiredArgsConstructor
public class LdapUserApiAdapter {
  private final LdapApi ldapApi;
  public User fetchUserByUsername(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // i used a derived value from the original fieelds
    if (dto.getUid() == null) {
      dto.setUid("anonymous");
    }
    User user = new User(dto.getUid() == null ? "anonymous" : dto.getUid(), fullName, dto.getWorkEmail());
    return user;
  }
}
