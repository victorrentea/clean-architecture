package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnAdapter { // TM from GoF
  private final LdapApi ldapApi;
  public User fetchUserDetailsFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String username = dto.getUn();
    if (username.startsWith("s")) {// eg 's12051' is a system user
      username = "system";
    }

    return new User(username, fullName, Optional.ofNullable(dto.getWorkEmail()));
  }

}
