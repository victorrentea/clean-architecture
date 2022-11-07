package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service // here, there shall be peace. harmony. zen. ying and yang. Biz Logic ! in a clean, friendly tested env.
public class UserService {
   private final IUserProviderAdapter adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.fetchUser(username);
//      LdapUserDto
//      CustomerDto
      if (user.workEmail().isPresent()) {
         log.debug("Send welcome email to  " + user.workEmail().get());
      }

      log.debug("Insert user in my database: " + user.username());


      log.debug("More business logic with " + user.fullname() +
                " of id " + user.username().toLowerCase());

   }


   //   private void innocentFix(User user) {
//      if (user.getUsername() == null) {
//         user.setUsername("N/A");
//      }
//   }


}
