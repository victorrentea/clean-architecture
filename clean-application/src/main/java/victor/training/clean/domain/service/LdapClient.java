package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

// Adapter Pattern: sa protejeze ceva la care tin de exterior
@RequiredArgsConstructor
@Adapter
public class LdapClient {
  private final LdapApi ldapApi;

  public User retrieveUser(String uid) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, uid.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + uid + ", got: " + dtoList);
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
