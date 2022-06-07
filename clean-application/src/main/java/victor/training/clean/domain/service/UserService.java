package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.entity.User;
import victor.training.clean.infra.LdapUserDto;
import victor.training.clean.infra.LdapUserServiceAdapter;

@RequiredArgsConstructor
@Slf4j
@Service
// holy domain service : the most precious code you have
public class UserService {
   private final LdapUserServiceAdapter adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.getUserByUsername(username);

      // imagine biz logc logic
      if (user.getWorkEmail() != null) {
         log.debug("Send welcome email to " + user.getWorkEmail());
      }
      log.debug("Insert user in my database");
      log.debug("More business logic with " + user.getFullName());
   }
}

