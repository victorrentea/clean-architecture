package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LdapClient {
  private final LdapApi ldapApi;

  public User fetchOneUserByUsername(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);


    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    // TODO victorrentea 2023-03-24:

    String username = dto.getUid();
    if (username == null) {
      username = "anonymous";
    }
    return new User(username, dto.getWorkEmail(), fullName);
  }

}
