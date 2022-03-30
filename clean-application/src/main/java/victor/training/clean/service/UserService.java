package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.User;

@Slf4j
@Service // DOMAIN LOGIC ONLY. PACE. ARMONIE. ZEN. YING si frasu'
@RequiredArgsConstructor
public class UserService {
   private final Adapter adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.getUserFromLdap(username);

      if (user.getWorkEmail().isPresent()) {
         log.debug("Send welcome email to " + user.getWorkEmail().get().toLowerCase());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }

}

