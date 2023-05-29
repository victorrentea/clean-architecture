package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApiClient;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final LdapApiClient ldapApiClient;

  public void importUserFromLdap(String targetUsername) {
    User user = ldapApiClient.findByUserName(targetUsername);
    // ⚠️ many useless fields
    if (user.getEmail() != null) { // ⚠️ NPE in other unguarded places?
      checkNewUser(user.getEmail().toLowerCase());
    }

    // ⚠️ 'uid' <- ugly attribute name; I'd prefer to see 'username', my domain term
    log.debug("Insert user in my database: " + user.getUserName());

    log.debug("More logic for " + user.getFullName() + " of id " + user.getUserName().toLowerCase());

    sendMailTo(user.getEmailContact());

    // then later, again (⚠️ repeated logic):
    sendMailTo(user.getEmailContact());
  }


  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
