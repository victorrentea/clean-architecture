package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserLdapAdapter userLdapAdapter;

    public void importUserFromLdap(String targetUsername) {
        complexLogic(userLdapAdapter.getUserByUsername(targetUsername));
    }

    private void complexLogic(User user) {
        if (user.getWorkEmail().isPresent()) {
            checkNewUser(user.getWorkEmail().get());
        }

        log.debug("Insert user in my database: " + user.getUserName());
        log.debug("More logic for " + user.getFullName() + " of id " + user.getUserName());

        if (user.getWorkEmail().isPresent()) {
            sendMailTo(user.getToAddress());
            sendMailTo(user.getToAddress());
        }
    }


    private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
        //... implementation left out
        log.debug("Contact: " + emailContact);
    }

    public void checkNewUser(String email) {
        log.debug("Check this user is not already in my system  " + email);
    }

}
