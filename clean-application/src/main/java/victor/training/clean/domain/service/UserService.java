package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final LdapApi ldapApi;

  public void importUserFromLdap(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username "
                                         + targetUsername + ", got: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // Victor RENTEA
    String username = dto.getUid() != null ? dto.getUid() : "anonymous";

    User user = new User(username, dto.getWorkEmail(), fullName);

    complexLogic(user);
  }

  private void complexLogic(User user) {
    user.getEmail().ifPresent(e->checkNewUser(e));

    log.debug("More logic for " + user.getFullName() + " of id " + user.getUsername());

    // avoid calling this if the user has no email
    user.asEmailContact().ifPresent(c->sendMailTo(c));

    user.asEmailContact().ifPresent(c->sendMailTo(c));
  }

  //  private void normalize(User dto) {
//    if (dto.getUid() == null) {
//      dto.setUid("anonymous"); // ⚠️ dirty hack
//    }
//  }

  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  private void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
