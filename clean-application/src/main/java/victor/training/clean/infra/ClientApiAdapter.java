package victor.training.clean.infra;

import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.ExternalUserProvider;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@Component
public class ClientApiAdapter implements ExternalUserProvider {
  private final LdapApi ldapApi;

  public ClientApiAdapter(LdapApi ldapApi) {
    this.ldapApi = ldapApi;
  }
  @Override
  public User fetchUserByUsername(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    User user = new User(dto.getUid() == null ?"anonymous":dto.getUid(), dto.getWorkEmail(), fullName);
    return user;
  }

}
