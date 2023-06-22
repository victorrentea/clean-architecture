package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;

import java.util.List;

@RequiredArgsConstructor
@Adapter
public class LdapApiClient {
  private final LdapApi ldapApi;


  public User loadUserByUsername(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username "
                                         + targetUsername + ", got: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // Victor RENTEA
    String username = dto.getUid() != null ? dto.getUid() : "anonymous";

    User user = new User(username, dto.getWorkEmail(), fullName);
    return user;
  }
}
