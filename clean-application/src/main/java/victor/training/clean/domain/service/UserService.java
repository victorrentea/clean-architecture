package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final UserProvider userProvider;

  public void importUserFromLdap(String targetUsername) {
    User user = userProvider.getUserByUsername(targetUsername);

    complexLogic(user);
  }



  private void complexLogic(User user) {
    user.getEmail().ifPresent(this::checkNewUser);

    log.debug("More logic for " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // ⚠️ 'uid' <- ugly; Users have a 'username' in my domain

    user.getEmailContact().ifPresent(this::sendMailTo);

    // then later, again (⚠️ repeated logic):
    user.getEmailContact().ifPresent(this::sendMailTo);
  }


  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  private void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
