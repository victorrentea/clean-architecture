package victor.training.clean.infra;

import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.ILdapAdapter;

import java.util.List;
import java.util.Optional;

@Service
public class LdapAdapter implements ILdapAdapter {
  private final LdapApi ldapApi;

  public LdapAdapter(LdapApi ldapApi) {
    this.ldapApi = ldapApi;
  }

  @Override
  public User fetchUser(String userId) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserDetailsFromLdap(userId);
    // ⚠️ data mapping mixed with my core domain logic TODO pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    String username;
    if (ldapUserDto.getUn().startsWith("s")) {// eg 's12051' is a system user
      username = "system"; // ⚠️ dirty hack
    } else {
      username = ldapUserDto.getUn();
    }
    return new User(
        username,
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()));
  }

  private LdapUserDto fetchUserDetailsFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    return dtoList.get(0);
  }

}
