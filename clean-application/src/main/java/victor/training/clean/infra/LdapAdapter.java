package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class LdapAdapter {
    private final LdapApi ldapApi;


  public User lookupUser(String userId) { // BEWARE: NETWORK API CALL INVOLVED
    // ⚠️ external DTO directly used in my app
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null); // fName???

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    String username = ldapUserDto.getUn();
    if (username.startsWith("s")) {// eg 's12051' is a system user
     username ="system"; // ⚠️ dirty hack
   }
    User user = new User(username, fullName, Optional.ofNullable(ldapUserDto.getWorkEmail()));
    return user;
  }

}
