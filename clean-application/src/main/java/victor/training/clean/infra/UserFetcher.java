package victor.training.clean.infra;
// there are only two things hard in computer science: cache invalidation and naming things

//class UserRepo {// smells like DB access time 2-5ms
//class UserService {// does not suggest
//class UserLoader {//
//class LdapService {// too impl specific
//class UserApiClient {// !!! API CALL
//class UserApiAdapter {// !!! API CALL

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFetcher implements victor.training.clean.domain.service.IUserFetcher {// !!! API CALL
  private final LdapApi ldapApi;

  @Override
  public User fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    if (dto.getUn().startsWith("s")) {
      dto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
    return new User(dto.getUn(),
        dto.getFname() + " " + dto.getLname().toUpperCase(),
        Optional.ofNullable(dto.getWorkEmail()));
  }

}