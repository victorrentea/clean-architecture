package victor.training.clean.domain.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.insurance.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
// holy domain service : the most precious code you have
public class UserService {
   private final ExternalUserService externalUserService;

   public void importUserFromLdap(String username) {
      User user = externalUserService.getUserByUsername(username);
      // imagine biz logc logic
      if (user.hasEmail()) {
         log.debug("Send welcome email to " + user.getWorkEmail());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}

