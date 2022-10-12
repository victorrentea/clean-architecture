package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
   private final ILdapServiceAdapter adapter;

   public void importUserFromLdap(String username) {
      User user = adapter.retrieveUserByUsername(username);

      // 60 teste
      if (user.getEmail().isPresent()) {
         log.debug("Send welcome email to  " + user.getEmail().get());
      }
      // 70 de linii mai jos cineva face un apel

      log.debug("Insert user in my database: " + user.getUsername());

      log.debug("More business logic with " + user.getCorporateName() +
                " of id " + user.getUsername().toLowerCase());
   }


}
