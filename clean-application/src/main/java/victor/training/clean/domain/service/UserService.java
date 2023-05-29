package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final UserGateway userGateway;

  public void importUserFromLdap(String targetUsername) {
    User user = userGateway.findByUserName(targetUsername);

    user.getEmail().ifPresent(this::checkNewUser);

    log.debug("Insert user in my database: " + user.getUserName());
    log.debug("More logic for " + user.getFullName() + " of id " + user.getUserName().toLowerCase());

    user.getEmailContact().ifPresent(this::sendMailTo);
    user.getEmailContact().ifPresent(this::sendMailTo);
  }

  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
