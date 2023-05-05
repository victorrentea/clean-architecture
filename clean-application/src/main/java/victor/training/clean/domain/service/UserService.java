package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapClient;


//Sfantul Domain Service, doar cu logica MEA, pentru care sunt platit sa fac app asta.
// #fiicunoinufiigunoi
@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final LdapClient ldapClient;

  public void importUserFromLdap(String targetUsername) {
    User user = ldapClient.retrieveUser(targetUsername);

    user.getEmail().ifPresent(this::checkNewUser);

    log.debug("Insert user in my database: " + user.getUserRct());

    log.debug("More logic for " + user.getFullName() + " of id " + user.getUserRct().toLowerCase());

    user.getEmailContact().ifPresent(this::sendMailTo);

    // then later, again (⚠️ repeated logic):
    user.getEmailContact().ifPresent(this::sendMailTo);
  }


  private void sendMailTo(String emailContact) { // don't change this <- it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
