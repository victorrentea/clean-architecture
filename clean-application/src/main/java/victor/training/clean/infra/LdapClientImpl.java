package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.LdapClient;

import java.util.List;

// Adapter Pattern: sa protejeze ceva la care tin de exterior
@RequiredArgsConstructor
@Adapter
public class LdapClientImpl implements LdapClient {
  private final LdapApi ldapApi;

  @Override
  public User retrieveUser(String username) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + username + ", got: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);
    return fromDto(dto);
  }

  private static User fromDto(LdapUserDto dto) {
    // - am creat un nou ob de domain:
    //- mic: cu strict campurile necesare
    //- guardate (anonymous)
    //- transformate (fullName)
    //- nume bune (username)
    //- logica in el (getEmailContact)
    //- si null-safe (Optional)
    //- immutable (nu am temporal coupling)
    String userRct = dto.getUid() != null ? dto.getUid() : "anonymous";
    // ⚠️ data mapping mixed with biz logic
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    return new User(userRct, dto.getWorkEmail(), fullName);
  }
}
