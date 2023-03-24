package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final ILdapClient ILdapClient;

  public void importUserFromLdap(String targetUsername) {
    User user = ILdapClient.fetchOneUserByUsername(targetUsername);

    user.getEmail().map(String::toLowerCase).ifPresent(this::checkNewUser);

    // ⚠️ 'uid' <- ugly attribute name; I'd prefer to see 'username', my domain term
    log.debug("Insert user in my database: " + user.getUsername());
//LdapUserDto c;
    //    fixUser(dto); // ⚠️ temporal coupling with the next line
    log.debug("More logic for " + user.getFullName() + " of id " + user.getUsername().toLowerCase());

    user.asEmailContact().ifPresent(this::sendMailTo);
    // then later, again (⚠️ repeated logic):
    user.asEmailContact().ifPresent(this::sendMailTo);
  }



  private void sendMailTo(String emailContact) { // don't change this <- it's library code
    //... implementation left out
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
