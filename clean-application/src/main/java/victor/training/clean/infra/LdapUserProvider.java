package victor.training.clean.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserProvider;

import java.util.List;

@Component
public class LdapUserProvider implements UserProvider {
  @Autowired
  private LdapApi ldapApi;

  @Override
  public User getUserByUsername(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
    }

    return fromDto(dtoList);
  }

  private static User fromDto(List<LdapUserDto> dtoList) {
    LdapUserDto dto = dtoList.get(0);
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    return new User(dto.getUid(), dto.getWorkEmail(), fullName);
  }
}
