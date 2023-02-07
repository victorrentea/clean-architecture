package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import victor.training.clean.common.DomainService;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@DomainService // ce-ai tu mai sfant in app, aici e
public class UserService {
  private final LdapUserClientInterface client;
  // ba nu e ok

  public void importUserFromLdap(String username) {
    User user = client.fetchUserByUsername(username);

    if (user.getEmail().isPresent()) {
      log.debug("Send welcome email to  " + user.getEmail().get());
    }

    log.debug("Insert user in my database: " + user.getUsername());

    log.debug("More " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // ⚠️ pending NullPointerException

    // then, in multiple places:
    user.asEmailContact().ifPresent(this::sendMailTo);

    user.asEmailContact().ifPresent(this::sendMailTo);
    user.asEmailContact().ifPresent(this::sendMailTo);
    user.asEmailContact().ifPresent(this::sendMailTo);
  }


  private void sendMailTo(String emailContact) {
    System.out.println("Contact: " + emailContact);
    //..implem left out
  }

}
