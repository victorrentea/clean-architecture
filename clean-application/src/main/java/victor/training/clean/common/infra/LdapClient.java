package victor.training.clean.common.infra;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import victor.training.clean.common.entity.User;
import victor.training.clean.common.ExternalUserProvider;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Setter
public class LdapClient implements ExternalUserProvider { // = Adapter design pattern
  private final LdapApi ldapApi;

  @Override
  public User retrieveUserById(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();

    String uid = dto.getUid();
    if (uid == null) {
      uid = "anonymous";
    }
    return new User(uid, dto.getWorkEmail(), fullName);
  }
}
