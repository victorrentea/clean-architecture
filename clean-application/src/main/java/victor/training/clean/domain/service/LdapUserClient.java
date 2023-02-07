package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

// detaliile interactiunii cu LDAP sunt aici
@RequiredArgsConstructor
@Component // ADapter pattern (TM)
public class LdapUserClient {
  private final LdapApi ldapApi;

  public User fetchUserByUsername(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(
            null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // ⚠️ data mapping mixed with biz logic
    String uname = dto.getUid() != null ? dto.getUid() : "anonymous";
    User user = new User(uname, dto.getWorkEmail(), fullName);
    return user;
  }
}
