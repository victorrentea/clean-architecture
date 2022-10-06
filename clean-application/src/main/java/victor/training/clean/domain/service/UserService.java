package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
   private final ExternalUserProvider adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.findUserByUsername(username);

      if (user.getWorkEmail().isPresent()) {
         log.debug("Send welcome email to  " + user.getWorkEmail().get());

      }
      // IMAGINE DRAGONS: 30 tests on this code are required
      log.debug("Insert user in my database: " + user.getUsername());

      log.debug("More business logic with " + user.getCorporateName() +
                " of id " + user.getUsername().toLowerCase());
   }


}
