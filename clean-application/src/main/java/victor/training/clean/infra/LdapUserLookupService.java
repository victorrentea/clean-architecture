package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.service.UserLookupService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LdapUserLookupService implements UserLookupService {
  private final LdapApi ldapApi;

  @Override
  public User lookupUser(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    // mapping, null checks, default, normalization
    // more dark stuff here:
    // retry
    // aggregation of data from 2+ api
    // async-api: they give you a URL because the work takes too long, and you poll every 2 seconds if it's done.
    // certificates
    // jwt tokens
    // rate limiters
    // SOAP / Corba
    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String username = dto.getUn();
    if (username.startsWith("s")) {
      username = "system"; // dirty hack: replace any system user with 'system'
    }

    String workEmail = dto.getWorkEmail() != null ? dto.getWorkEmail().toLowerCase() : null;

    return new User(username, fullName, workEmail);
  }
}
