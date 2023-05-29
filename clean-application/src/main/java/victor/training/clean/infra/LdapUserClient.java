package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.LdapUserClientInterface;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

// Adapter design pattern in GoF
//@Component
@RequiredArgsConstructor
@Adapter
public class LdapUserClient implements LdapUserClientInterface {
  private final LdapApi ldapApi;

  @Override
  public User fetchByUsername(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
    }
    LdapUserDto dto = dtoList.get(0);

    return fromDto(dto);
  }

  private static User fromDto(LdapUserDto dto) {
//    String username = dto.getUid();
//    if (username == null) {
//      username = "anonymous";
//    }

//    String username = Optional.ofNullable(dto.getUid()).orElse("anonymous");
    String username = dto.getUid() != null ? dto.getUid() : "anonymous";

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String workEmail = dto.getWorkEmail() != null ? dto.getWorkEmail().toLowerCase():null;
    return new User(username, fullName, workEmail);
  }

}
