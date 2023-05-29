package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final LdapUserClient ldapUserClient;

  public void importUserFromLdap(String targetUsername) {
    User user = ldapUserClient.fetchByUsername(targetUsername);
    user.getEmail().ifPresent(this::checkNewUser);

    log.debug("Insert user in my database: " + user.getUsername());

    log.debug("More logic for " + user.getFullName() + " of id " + user.getUsername().toLowerCase());

    user.asEmailContact().ifPresent(this::sendMailTo);

    user.asEmailContact().ifPresent(this::sendMailTo);
  }


  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
