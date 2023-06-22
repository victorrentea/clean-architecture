package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final LdapApiClientInterface ldapApiClient;

  public void importUserFromLdap(String targetUsername) {
    User user = ldapApiClient.loadUserByUsername(targetUsername);

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
