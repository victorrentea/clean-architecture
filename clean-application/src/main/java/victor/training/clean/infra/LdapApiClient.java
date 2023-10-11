package victor.training.clean.infra;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.UserProvider;

import java.util.List;
import java.util.Optional;

@Adapter
@Slf4j
@RequiredArgsConstructor
public final class LdapApiClient implements UserProvider {
  private final LdapApi ldapApi;


  @Override
  @Timed
  public User retrieveUser(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    if (dto.getUn().startsWith("s")) {// eg s12051 - a system user
      dto.setUn("system"); // ⚠️ dirty hack
    }
    User user = new User(dto.getUn(), fullName, Optional.ofNullable(dto.getWorkEmail()));
    return user;
  }

}
