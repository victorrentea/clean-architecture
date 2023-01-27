package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final UserLdapApiClient client;

  // PR reject: class is doing things at a different level of abstraction: adica...
  //  faci [domain logic] sfant si [mapari/validari/apicalls] murdare.
  public void importUserFromLdap(String username) {
    User user = client.fetchUser(username);

    // ⚠️ useless fields -> i only need 3 not 7 fields
    if (user.getEmail().isPresent()) { // ⚠️ how about other unguarded places?
      log.debug("Send welcome email to  " + user.getEmail().get());
    }

    log.debug("Insert user in my database: " + user.getUsername()); // ⚠️ bad attribute name

    log.debug("More " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // ⚠️ pending NullPointerException

    // then, in multiple places:
    user.asEmailContact().ifPresent(c->sendMailTo(c)); // ⚠️ repeated logic
    user.asEmailContact().ifPresent(c->sendMailTo(c));
    user.asEmailContact().ifPresent(c->sendMailTo(c));
    user.asEmailContact().ifPresent(c->sendMailTo(c));
  }


  //  private void innocentHack(User dto) {
//    if (dto.getUid() == null) {
//      dto.setUid("anonymous"); // ⚠️ mutability risks
//    }
//  }

  private void sendMailTo(String emailContact) {
    System.out.println("Contact: " + emailContact);
    //..implem left out
  }

}
