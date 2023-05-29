package victor.training.clean.infra;

import java.util.List;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.LdapUser;

@Component
public class LdapClient {

  private final LdapApi ldapApi;

  public LdapClient(LdapApi ldapApi) {
    this.ldapApi = ldapApi;
  }

  public LdapUser find(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

    // maybe add nullcheck for the list

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
    }

    return fromUserLdapDto(dtoList.get(0));
  }


  private LdapUser fromUserLdapDto(LdapUserDto dto) {
    return new LdapUser(
        dto.getUid() == null ? "anonymous" : dto.getUid(),
        dto.getFname() + " " + dto.getLname().toUpperCase(),
        dto.getWorkEmail()
    );
  }

}
