package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService { // DOmain!!!
  private final ILdapApiAdapter adapter;

  public void importUserFromLdap(String username) {
    User user = adapter.fetchUserByUsername(username);
    // ⚠️ useless fields ; i only need 4 not 10
    if (user.getEmail().isPresent()) { // ⚠️ how about other unguarded places?
      log.debug("Send welcome email to  " + user.getEmail().get());
    }
//    LdapUserDto oups;

    log.debug("Insert user in my database: " + user.getUsername()); // ⚠️ bad attribute name -> "username"

    //    innocentHack(user);
    log.debug("More " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // ⚠️ pending NullPointerException

    // then, in multiple places:

    user.getEmailContact().ifPresent(this::sendMailTo);
    user.getEmailContact().ifPresent(this::sendMailTo);
    user.getEmailContact().ifPresent(this::sendMailTo);
    user.getEmailContact().ifPresent(this::sendMailTo);
  }


  //  private void innocentHack(User dto) {
//    if (dto.getUsername() == null) {
//      dto.setUsername("anonymous"); // ⚠️ mutability risks
//    }
//  }

  private void sendMailTo(String emailContact) {
    System.out.println("Contact: " + emailContact);
    //..implem left out
  }

}
